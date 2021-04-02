package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import java.util.Map
import javax.inject.Inject
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement
import mil.navy.nrl.ncarai.jasm.program.BufferDef
import mil.navy.nrl.ncarai.jasm.program.BufferPattern
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.ProgramFactory
import mil.navy.nrl.ncarai.jasm.program.RequestStatement
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.EcoreUtil2
import org.jactr.io2.jactr.comment.CommentUtilities
import org.jactr.io2.jactr.modelFragment.Buffer
import org.jactr.io2.jactr.modelFragment.ChunkDef
import org.jactr.io2.jactr.modelFragment.ChunkType
import org.jactr.io2.jactr.modelFragment.Condition
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory
import org.jactr.io2.jactr.modelFragment.Modify
import org.jactr.io2.jactr.modelFragment.Production

class BufferRequestGenerator implements IProductionGenerator {
  @Inject extension ValidationUtilities

  override generate(Pattern currentGoal, Pattern finalGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof RequestStatement) {

      val priorState = context._goalStateProvider.priorGoalState
      var branched = false
      /*
       * three productions: request, match-map, fail-execute
       */
      var nextGoal = finalGoal 
      if (inst.mapping !== null) {
        context._goalStateProvider.startBranch(inst.mapping.block.instructions, context)
        branched = true
      } else if (inst.handler !== null) {
        context._goalStateProvider.startBranch(currentGoal.slots.last.value.name, context, "no_map")
        branched = true
      }
      if(branched)
        nextGoal = context._goalStateProvider.nextGoalState

      context.consumer.accept(buildRequestProduction(currentGoal, nextGoal, inst, context))


      var entryPattern = priorState
      if (inst.mapping !== null) {
        entryPattern = buildMappingProductions(nextGoal, finalGoal, inst, context)
      } else if (inst.handler !== null) {
        entryPattern = buildNoMappingProduction(nextGoal, finalGoal, inst, context)
        context._goalStateProvider.nextState
      }
      

      if (inst.handler !== null) {
        context.consumer.accept(buildRequestFailedProduction(entryPattern, finalGoal, inst, context))
      }

      if (branched)
        context._goalStateProvider.endBranch
    }
  }

  def buildRequestFailedProduction(Pattern currentGoal, Pattern nextGoal, RequestStatement request,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    val pName = context._goalStateProvider.currentFunction.name + "-request-failed-" + request.buffer.name + "-Line" +
      ProcGenUtilities.lineNumber(request)
    production.name = pName

    val goal = ProcGenUtilities.toMatch(currentGoal, context)
    goal.name = context._resolver.resolveBuffer("goal")
    production.conditions.add(goal)

    context._goalManipulator.newGoal.ifPresent [ match |
      production.conditions.add(match)
    ]

    val error = ModelFragmentFactory.eINSTANCE.createQuery
    error.name = request.buffer
    var slot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS)
    slot.value.name = "error"
    error.slots.add(slot)

    production.conditions.add(error)

    // generate the block..
    context._goalStateProvider.startBranch(request.handler.block.instructions, context)
    val ngp = context._goalStateProvider.nextGoalState

    var modify = ProcGenUtilities.toModify(ngp, context)
    modify.name = context._resolver.resolveBuffer("goal")
    production.actions.add(modify)

    context._productionSequencer.sequence(request.handler.block.instructions, context)

    context._goalStateProvider.endBranch()

    production
  }

  def buildMappingProductions(Pattern currentGoal, Pattern nextGoal, RequestStatement request,
    ProceduralGenerationContext context) {

    context._productionSequencer.sequence(request.mapping.block.instructions, context)

    currentGoal
  }

  def buildNoMappingProduction(Pattern currentGoal, Pattern nextGoal, RequestStatement request,
    ProceduralGenerationContext context) {

    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    val pName = context._goalStateProvider.currentFunction.name + "-noMap-" + request.buffer.name + "-Line" +
      ProcGenUtilities.lineNumber(request)
    rtn.name = pName
    val goalBuffer = context._resolver.resolveBuffer("goal")
    val match = ProcGenUtilities.toMatch(currentGoal, context)
    match.name = goalBuffer
    rtn.conditions.add(match)

    val bufferDesc = bufferDefinition(request.buffer.name, request)
    var hasResult = true

    if (bufferDesc !== null) {
      val req = getMatchingRequestInfo(bufferDesc, request, context)
      if (req !== null && !req.wildcardResult && req.resultType === null)
        hasResult = false
    }

    if (bufferDesc === null || hasResult) {

      val requestVerification = ModelFragmentFactory.eINSTANCE.createMatch
      requestVerification.name = request.buffer
      requestVerification.isa = ModelFragmentFactory.eINSTANCE.createIsaBlock
      requestVerification.isa.name = "=" + request.buffer.name
      rtn.conditions.add(requestVerification)
    }

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = goalBuffer
    rtn.actions.add(modify)

    if (hasResult) {
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = request.buffer
      rtn.actions.add(touch)
    }

    // we need the chunktype info implied even without the mapping
    updateInferredContents(request, context)

    context.consumer.accept(rtn)

    currentGoal
  }

  def buildRequestProduction(Pattern currentGoal, Pattern nextGoal, RequestStatement request,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    val pName = context._goalStateProvider.currentFunction.name + "-request-" + request.buffer.name + "-Line" +
      ProcGenUtilities.lineNumber(request)
    production.name = pName

    var conditions = null as Map<Buffer, BufferPattern>
    val goalBuffer = context._resolver.resolveBuffer("goal")
    var requestPattern = null as Pattern
    var requestLiteral = null as ChunkDef
    var requestVariable = null as String

    val bindings = newArrayList()
    if (request.bindings !== null)
      bindings.addAll(request.bindings.bindings)

    /*
     * There are types of patternReferences to consider:
     * 1) chunk literal (buffer=null,name=chunkName). 
     * 2) buffer reference (buffer=null,name=bufferName).
     * 3) goal slot shorthand reference (buffer=null, name=slotName). 
     * 4) buffer-slot reference (buffer=buffer, name=slotName)
     * 5) pattern (buffer=null, name=patternName)
     */
    /*
     * we need to create binding bufferPatterns specific to 1-5. 
     */
    var referenceBuffer = request.patternReference.buffer
    /*
     * 1,2,3,5
     */
    if (referenceBuffer === null) {
      /*
       * if it's a pattern (5) no need to do anything.
       */
      if (isPattern(request.patternReference.name, request)) {
        requestPattern = patternDefinition(request.patternReference.name, request)
      } else
      /*
       * (3), and leave the processing to the same place we do (4)
       */
      if (isGoalSlot(request.patternReference.name, request, context)) {
        referenceBuffer = goalBuffer
      } else
      /*
       * (2), and create a custom binding
       */
      if (isBuffer(request.patternReference.name, request)) {

        /*
         * create a buffer pattern to describe this buffer reference
         */
        val bPattern = ProgramFactory.eINSTANCE.createBufferPattern
        bPattern.buffer = context._resolver.resolveBuffer(request.patternReference.name)
        bPattern.pattern = ProgramFactory.eINSTANCE.createPattern
        bPattern.pattern.name = "tmp"
        bPattern.pattern.type = context._bufferStateManager.getContentType(bPattern.buffer.name).get().name

        requestVariable = "=" + bPattern.buffer.name

        bindings.add(bPattern)
      } else {
        /*
         * (1) chunk literal, no binding needed
         */
        requestLiteral = context._resolver.resolveChunk(request.patternReference.name)
      }
    }

    /*
     * if we are requesting a buffer (3) or buffer.slot (4) value, we add them to the bindings set. 
     */
    if (referenceBuffer !== null && request.patternReference.name !== null) {
      /*
       * create a buffer pattern to describe this buffer.slot reference
       */
      val bPattern = ProgramFactory.eINSTANCE.createBufferPattern
      bPattern.buffer = referenceBuffer
      bPattern.pattern = ProgramFactory.eINSTANCE.createPattern
      bPattern.pattern.name = "tmp"

      bPattern.pattern.type = context._bufferStateManager.getContentType(bPattern.buffer.name).get().name

      val slot = ProcGenUtilities.conditionalSlot(request.patternReference.name, Condition.EQUALS)
      requestVariable = "=" + bPattern.buffer.name + "-" + request.patternReference.name
      slot.value.name = requestVariable
      bPattern.pattern.slots.add(slot)

      bindings.add(bPattern)
    }

    /*
     * merge all the bindings together into a series of conditions
     */
    conditions = ProcGenUtilities.mergePatterns(currentGoal, bindings, context)
    // handle goal first
    production.conditions.add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context))

    /*
     * and the rest
     */
    conditions.values.forEach [ bPattern |
      production.conditions.add(ProcGenUtilities.toMatch(bPattern, context))
    ]

    /*
     * make sure that the requesting buffer is empty (unless forced) and not busy
     */
    val emptyNotBusy = ModelFragmentFactory.eINSTANCE.createQuery
    emptyNotBusy.name = request.buffer
    var slot = ProcGenUtilities.conditionalSlot("state", Condition.NOT)
    slot.value.name = "busy"
    emptyNotBusy.slots.add(slot)

    if (!request.forced) {
      slot = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS)
      slot.value.name = "empty"
      emptyNotBusy.slots.add(slot)
    }

    production.conditions.add(emptyNotBusy)

    /*
     * actions
     */
    /*
     * (5)
     */
    if (requestPattern !== null) {
      val addRequest = ProcGenUtilities.toAdd(requestPattern, context)
      addRequest.name = request.buffer
      production.actions.add(addRequest)
    }
    /*
     * (1), custom add
     */
    if (requestLiteral !== null) {
      val addRequest = ModelFragmentFactory.eINSTANCE.createAdd
      addRequest.name = request.buffer
      addRequest.isa = ModelFragmentFactory.eINSTANCE.createIsaBlock
      addRequest.isa.chunk = requestLiteral
      production.actions.add(addRequest)
    }
    /*
     * (2-4) custom add of variable reference
     */
    if (requestVariable !== null) {
      val addRequest = ModelFragmentFactory.eINSTANCE.createAdd
      addRequest.name = request.buffer
      addRequest.isa = ModelFragmentFactory.eINSTANCE.createIsaBlock
      addRequest.isa.name = requestVariable
      production.actions.add(addRequest)
    }

    /*
     * update infered contents. use the cast overrides. otherwise  
     */
    updateInferredContents(request, context)

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = context._resolver.resolveBuffer("goal")
    production.actions.add(modify)

//touch
    if (conditions !== null) {
      conditions.keySet.forEach [ buffer |
        val touch = ModelFragmentFactory.eINSTANCE.createModify
        touch.name = buffer
        production.actions.add(touch)
      ]
    }

    CommentUtilities.comment(production, ProcGenUtilities.text(request));

    production
  }

  override handles(Instruction instr) {
    return instr instanceof RequestStatement
  }

  protected def isGoalSlot(String slotName, EObject inFunction, ProceduralGenerationContext context) {
    val func = EcoreUtil2.getContainerOfType(inFunction, Function)
    val chunkType = context._resolver.resolveChunkType(func.name)
    return chunkType.slots.findFirst[slot|slot.name.equals(slotName)] !== null
  }

  protected def getMatchingRequestInfo(BufferDef bufferDesc, RequestStatement request,
    ProceduralGenerationContext context) {

    val wildCardRequest = bufferDesc.requests.findFirst [ req |
      req.wildcardRequest
    ]
    if (wildCardRequest !== null)
      return wildCardRequest

    val patternDef = patternDefinition(request.patternReference.name, request)
    val requestType = context._resolver.resolveChunkType(patternDef.type)
    /*
     * in depth check
     */
    for (req : bufferDesc.requests) {
      if (!req.wildcardRequest)
        if (requestType.isA(req.requestType)) {
          return req
        }
    }
    return null
  }

  protected def updateInferredContents(RequestStatement request, ProceduralGenerationContext context) {
    /*
     * 
     */
    if (request.cast !== null)
      context._bufferStateManager.setContentType(request.buffer.name, getChunkType(request, request.cast))
    else {
      /*
       * the only two conditions that don't require a cast are pattern and buffer direct
       */
      var requestType = null as ChunkType
      val patternDef = patternDefinition(request.patternReference.name, request)
      if (patternDef !== null)
        requestType = context._resolver.resolveChunkType(patternDef.type)
      else
        requestType = context._bufferStateManager.getContentType(request.patternReference.name).get()

      val bufferDesc = bufferDefinition(request.buffer.name, request)
      if (bufferDesc === null)
        context._bufferStateManager.setContentType(request.buffer.name, requestType)
      else {
        val req = getMatchingRequestInfo(bufferDesc, request, context)

        if (req === null)
          throw new IllegalStateException("Request of " + request.buffer.name + " with " + requestType.name +
            " has no matching result type. Check buffer meta defintions or your code.")

        if (req.wildcardResult) {
          context._bufferStateManager.setContentType(request.buffer.name, requestType)
        } else {
          if (req.requestType === null) { // empty
            context._bufferStateManager.setContentType(request.buffer.name, null)
          } else
            context._bufferStateManager.setContentType(request.buffer.name, req.resultType)
        }
      }
    }
  }
}
