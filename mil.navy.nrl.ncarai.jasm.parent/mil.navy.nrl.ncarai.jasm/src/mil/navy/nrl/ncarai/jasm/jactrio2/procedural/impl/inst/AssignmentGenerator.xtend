package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import java.util.Optional
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.ProgramFactory
import org.eclipse.emf.ecore.util.EcoreUtil
import org.jactr.io2.jactr.modelFragment.Buffer
import org.jactr.io2.jactr.modelFragment.Condition
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class AssignmentGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof AssignmentStatement) {
      if (inst.assignment.functionCall !== null) {
        val function = inst.assignment.functionCall
        /*
         * if we have a function, we need to branch and dispatch.
         */
        context._goalStateProvider.startBranch(currentGoal.slots.last.value.name, context, "executing")
        val executingState = context._goalStateProvider.nextGoalState

        context.consumer.accept(
          FunctionalCallGenerator.createInitialProduction(currentGoal, executingState, function, function.function,
            context))

        context._goalStateProvider.nextState

        if (function.function.isChunk)
          context.consumer.accept(
            FunctionalCallGenerator.createTerminalProduction(executingState, nextGoal, inst, function.function,
              context, "=return"))
        else if (function.function.isVoid)
          context.consumer.accept(
            FunctionalCallGenerator.createTerminalProduction(executingState, nextGoal, inst, function.function,
              context, "null"))

        context._goalStateProvider.endBranch

      } else {

        context.consumer.accept(createSimpleAssignmentProduction(currentGoal, nextGoal, inst, null, context))
        val nullProduction = createSimpleAssignmentNullProduction(currentGoal, nextGoal, inst, null, context)
        if (nullProduction !== null)
          context.consumer.accept(nullProduction)
      }

    }
  }

  override handles(Instruction instr) {
    instr instanceof AssignmentStatement
  }

  static def createSimpleAssignmentProduction(Pattern currentGoal, Pattern nextGoal, AssignmentStatement inst, Buffer bufferToQuery,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = context._goalStateProvider.currentFunction.name + "-assignment-Line" +
      ProcGenUtilities.lineNumber(inst)
    /*
     * otherwise, we just create a single production to do this assignment
     */
    val goalBuffer = context._resolver.resolveBuffer("goal")
    var cgp = currentGoal
    var match = matchAsBufferPattern(inst, context)
    /*
     * if it's goal match, we merge it with the desired goal 
     */
    if (match.isPresent) {
      if (match.get.buffer === goalBuffer)
        cgp = ProcGenUtilities.merge(cgp, match.get.pattern, context)
    }

    val goalCondition = ProcGenUtilities.toMatch(cgp, context)
    goalCondition.name = goalBuffer
    production.conditions.add(goalCondition)
    
    if(bufferToQuery!==null)
    {
      val query = ModelFragmentFactory.eINSTANCE.createQuery
      query.name = bufferToQuery
      /*
       * buffer = full
       * state = free 
       */
       val bSlot = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS)
       bSlot.value.name = "full"
       query.slots.add(bSlot)
       
       val sSlot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS)
       sSlot.value.name = "free"
       query.slots.add(sSlot)
       
       production.conditions.add(query)
    }

    if (inst.reference.buffer !== null && inst.reference.buffer !== goalBuffer) {
      // we need a binding condition for this buffer
      val chunkType = context._bufferStateManager.getContentType(inst.reference.buffer.name)
      val bindingCondition = ModelFragmentFactory.eINSTANCE.createMatch
      bindingCondition.name = inst.reference.buffer
      bindingCondition.isa = ModelFragmentFactory.eINSTANCE.createIsaBlock
      if (chunkType.isPresent)
        bindingCondition.isa.type = chunkType.get
      else
        bindingCondition.isa.name = "=" + inst.reference.buffer.name

      production.conditions.add(bindingCondition)
    }


    if (match.isPresent && match.get.buffer !== goalBuffer) {
      val bindingCondition = ProcGenUtilities.toMatch(match.get.pattern, context)
      bindingCondition.name = match.get.buffer
      production.conditions.add(bindingCondition)
    }

    /*
     * repeat for the action, with possible merging
     * of the pattern. 
     */
    var ngp = nextGoal
    var modify = modifyAsBufferPattern(inst, context)
    if (modify.isPresent && modify.get.buffer === goalBuffer)
      ngp = ProcGenUtilities.merge(ngp, modify.get.pattern, context)

    val goalAction = ProcGenUtilities.toModify(ngp, context)
    goalAction.name = goalBuffer
    production.actions.add(goalAction)

    if (modify.isPresent && modify.get.buffer !== goalBuffer) {
      val bindingCondition = ProcGenUtilities.toModify(modify.get.pattern, context)
      bindingCondition.name = modify.get.buffer
      production.actions.add(bindingCondition)
    }
    /*
     * now we need to insert a touch/modify to prevent strict harvesting. 
     * we insert if match exists and modify doesn't exist or refers to a different buffer.
     */
    if ((match.isPresent && match.get.buffer !== goalBuffer) &&
      (!modify.isPresent || modify.get.buffer !== match.get.buffer)) {
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = match.get.buffer
      production.actions.add(touch)
    }

    production
  }

  static def createSimpleAssignmentNullProduction(Pattern currentGoal, Pattern nextGoal, AssignmentStatement inst, Buffer bufferToQuery, 
    ProceduralGenerationContext context) {

    /**
     * if the assignment is a constant or a function, there is no need for the null production
     */
    if (isConstantOrFunctionAssignment(inst, context))
      return null

    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = context._goalStateProvider.currentFunction.name + "-null-assignment-Line" +
      ProcGenUtilities.lineNumber(inst)
    /*
     * otherwise, we just create a single production to do this assignment
     */
    val goalBuffer = context._resolver.resolveBuffer("goal")
    var cgp = currentGoal
    var match = matchAsBufferNullPattern(inst, context)
    /*
     * if it's goal match, we merge it with the desired goal 
     */
    if (match.isPresent) {
      if (match.get.buffer === goalBuffer)
        cgp = ProcGenUtilities.merge(cgp, match.get.pattern, context)
    }

    val goalCondition = ProcGenUtilities.toMatch(cgp, context)
    goalCondition.name = goalBuffer
    production.conditions.add(goalCondition)
    
    if(bufferToQuery!==null)
    {
      val query = ModelFragmentFactory.eINSTANCE.createQuery
      query.name = bufferToQuery
      /*
       * buffer = full
       * state = free 
       */
       val bSlot = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS)
       bSlot.value.name = "full"
       query.slots.add(bSlot)
       
       val sSlot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS)
       sSlot.value.name = "free"
       query.slots.add(sSlot)
       
       production.conditions.add(query)
    }

    if (inst.reference.buffer !== null && inst.reference.buffer !== goalBuffer) {
      // we need a binding condition for this buffer
      val chunkType = context._bufferStateManager.getContentType(inst.reference.buffer.name)
      val bindingCondition = ModelFragmentFactory.eINSTANCE.createMatch
      bindingCondition.name = inst.reference.buffer
      bindingCondition.isa = ModelFragmentFactory.eINSTANCE.createIsaBlock
      if (chunkType.isPresent)
        bindingCondition.isa.type = chunkType.get
      else
        bindingCondition.isa.name = "=" + inst.reference.buffer.name

      production.conditions.add(bindingCondition)
    }

    if (match.isPresent && match.get.buffer !== goalBuffer) {
      val bindingCondition = ProcGenUtilities.toMatch(match.get.pattern, context)
      bindingCondition.name = match.get.buffer
      production.conditions.add(bindingCondition)
    }

    /*
     * repeat for the action, with possible merging
     * of the pattern. 
     */
    var ngp = nextGoal
    var modify = modifyAsBufferNullPattern(inst, context)
    if (modify.isPresent && modify.get.buffer === goalBuffer)
      ngp = ProcGenUtilities.merge(ngp, modify.get.pattern, context)

    val goalAction = ProcGenUtilities.toModify(ngp, context)
    goalAction.name = goalBuffer
    production.actions.add(goalAction)

    if (modify.isPresent && modify.get.buffer !== goalBuffer) {
      val bindingCondition = ProcGenUtilities.toModify(modify.get.pattern, context)
      bindingCondition.name = modify.get.buffer
      production.actions.add(bindingCondition)
    }
    /*
     * now we need to insert a touch/modify to prevent strict harvesting. 
     * we insert if match exists and modify doesn't exist or refers to a different buffer.
     */
    if ((match.isPresent && match.get.buffer !== goalBuffer) &&
      (!modify.isPresent || modify.get.buffer !== match.get.buffer)) {
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = match.get.buffer
      production.actions.add(touch)
    }

    production
  }

  /**
   * return the match necessary to bind for this statement. or maybe the BufferPattern??
   */
  static def matchAsBufferPattern(AssignmentStatement statement, ProceduralGenerationContext context) {
    /*
     * convert the value portion of the assignment into a buffer match
     */
    val goalBuffer = context._resolver.resolveBuffer("goal")
    var buffer = statement.assignment.buffer
    if (buffer === null && statement.assignment.value.name !== null) {
      buffer = context._resolver.resolveBuffer(statement.assignment.value.name)
      if (buffer === null) // it's not a buffer, probably a slot or a constant, assume goal
        buffer = goalBuffer
    }

    /*
     * string, number or boolean values are quick and easy requiring no
     * match
     */
    if (statement.assignment.value.string !== null || statement.assignment.value.number !== null ||
      statement.assignment.value.boolean !== null || // context._functionTable.isFunction(statement.assignment.value.name)
      statement.assignment.functionCall !== null)
      return Optional.empty

    // this is just a constant symbol, no binding required
    val function = context._goalStateProvider.currentFunction
    var recognizedSlot = true

    if (buffer === goalBuffer) {
      recognizedSlot = function.slots.
        findFirst[slot|slot.assignment.reference.name == statement.assignment.value.name] !== null
      if (!recognizedSlot)
        recognizedSlot = function.variables.findFirst [ slot |
          slot.assignment.reference.name == statement.assignment.value.name
        ] !== null
    }

    if (statement.assignment.buffer === null && buffer == goalBuffer && !recognizedSlot)
      return Optional.empty

    /*
     * we need the chunktype of whatever is in the buffer.
     * for request-assignments we can grab the ancestor, and pull the cast field.
     * 
     */
    val buffPattern = ProgramFactory.eINSTANCE.createBufferPattern
    val chunkType = context._bufferStateManager.getContentType(buffer.name).get()
    buffPattern.buffer = buffer
    buffPattern.pattern = ProgramFactory.eINSTANCE.createPattern
    buffPattern.pattern.type = chunkType.name

    /*
     * now for the variable
     */
    if (buffer === goalBuffer || statement.assignment.buffer !== null) {
      var slot = ProcGenUtilities.conditionalSlot(statement.assignment.value.name, Condition.EQUALS)
      slot.value.name = "=" + buffer.name + "-" + statement.assignment.value.name
      buffPattern.pattern.slots.add(slot)
    }

    Optional.of(buffPattern)
  }

  static def matchAsBufferNullPattern(AssignmentStatement statement, ProceduralGenerationContext context) {

    /*
     * convert the value portion of the assignment into a buffer match
     */
    val goalBuffer = context._resolver.resolveBuffer("goal")
    var buffer = statement.assignment.buffer
    if (buffer === null && statement.assignment.value.name !== null) {
      buffer = context._resolver.resolveBuffer(statement.assignment.value.name)
      if (buffer === null) // it's not a buffer, probably a slot or a constant, assume goal
        buffer = goalBuffer
    }

    /*
     * string, number or boolean values are quick and easy requiring no
     * match
     */
    if (statement.assignment.value.string !== null || statement.assignment.value.number !== null ||
      statement.assignment.value.boolean !== null || // context._functionTable.isFunction(statement.assignment.value.name)
      statement.assignment.functionCall !== null)
      return Optional.empty

    // this is just a constant symbol, no binding required
    val function = context._goalStateProvider.currentFunction
    var recognizedSlot = true

    if (buffer === goalBuffer) {
      recognizedSlot = function.slots.
        findFirst[slot|slot.assignment.reference.name == statement.assignment.value.name] !== null
      if (!recognizedSlot)
        recognizedSlot = function.variables.findFirst [ slot |
          slot.assignment.reference.name == statement.assignment.value.name
        ] !== null
    }

    if (statement.assignment.buffer === null && buffer == goalBuffer && !recognizedSlot)
      return Optional.empty

    /*
     * we need the chunktype of whatever is in the buffer.
     * for request-assignments we can grab the ancestor, and pull the cast field.
     * 
     */
    val buffPattern = ProgramFactory.eINSTANCE.createBufferPattern
    val chunkType = context._bufferStateManager.getContentType(buffer.name).get()
    buffPattern.buffer = buffer
    buffPattern.pattern = ProgramFactory.eINSTANCE.createPattern
    buffPattern.pattern.type = chunkType.name

    /*
     * now for the variable
     */
    if (buffer === goalBuffer || statement.assignment.buffer !== null) {
      var slot = ProcGenUtilities.conditionalSlot(statement.assignment.value.name, Condition.EQUALS)
      slot.value.name = "null"
      buffPattern.pattern.slots.add(slot)
    }

    Optional.of(buffPattern)
  }

  static def modifyAsBufferPattern(AssignmentStatement statement, ProceduralGenerationContext context) {
    /*
     * convert the value portion of the assignment into a buffer match
     */
    var buffer = statement.reference.buffer
    if (buffer === null)
      buffer = context._resolver.resolveBuffer("goal")

    val bufferPattern = ProgramFactory.eINSTANCE.createBufferPattern
    bufferPattern.buffer = buffer
    bufferPattern.pattern = ProgramFactory.eINSTANCE.createPattern
    val slot = ProcGenUtilities.conditionalSlot(statement.reference.name, Condition.EQUALS)

    if (statement.assignment.value.name !== null && context._functionTable.isFunction(statement.assignment.value.name))
      return Optional.empty

    if ("null".equals(statement.assignment.value.name) || statement.assignment.value.string !== null ||
      statement.assignment.value.number !== null || statement.assignment.value.boolean !== null) {
      slot.value = EcoreUtil.copy(statement.assignment.value)
    } else if (statement.assignment.buffer !== null) {
      // variable
      slot.value.name = "=" + statement.assignment.buffer.name + "-" + statement.assignment.value.name
    } else {
      if (context._resolver.resolveBuffer(statement.assignment.value.name) !== null)
        slot.value.name = "=" + statement.assignment.value.name
      else {
        if (context._resolver.resolveChunk(statement.assignment.value.name) !== null)
          slot.value.name = statement.assignment.value.name
        else {
          // its a slot reference for goal
          slot.value.name = "=goal-" + statement.assignment.value.name
        }
      }
    }
    bufferPattern.pattern.slots.add(slot)

    Optional.of(bufferPattern)
  }

  static def isConstantOrFunctionAssignment(AssignmentStatement statement, ProceduralGenerationContext context) {
    if (statement.assignment.value.string !== null || statement.assignment.value.number !== null ||
      statement.assignment.value.boolean !== null || // context._functionTable.isFunction(statement.assignment.value.name)
      statement.assignment.functionCall !== null)
      return true

    // this is just a constant symbol, no binding required
    val function = context._goalStateProvider.currentFunction
    val goalBuffer = context._resolver.resolveBuffer("goal")

    var buffer = statement.assignment.buffer
    val loneBuffer = buffer === null && statement.assignment.value.name !== null
    if (buffer === null && statement.assignment.value.name !== null) {
      buffer = context._resolver.resolveBuffer(statement.assignment.value.name)
      if (buffer === null) // it's not a buffer, probably a slot or a constant, assume goal
        buffer = goalBuffer
    }
    var recognizedSlot = true

    if (buffer === goalBuffer) {
      recognizedSlot = function.slots.
        findFirst[slot|slot.assignment.reference.name == statement.assignment.value.name] !== null
      if (!recognizedSlot)
        recognizedSlot = function.variables.findFirst [ slot |
          slot.assignment.reference.name == statement.assignment.value.name
        ] !== null
    }

    if (statement.assignment.buffer === null &&
      ((buffer == goalBuffer && !recognizedSlot) || (loneBuffer && buffer != goalBuffer)))
      return true

    return false
  }

  static def modifyAsBufferNullPattern(AssignmentStatement statement, ProceduralGenerationContext context) {
    /*
     * convert the value portion of the assignment into a buffer match
     */
    var buffer = statement.reference.buffer
    if (buffer === null)
      buffer = context._resolver.resolveBuffer("goal")

    val bufferPattern = ProgramFactory.eINSTANCE.createBufferPattern
    bufferPattern.buffer = buffer
    bufferPattern.pattern = ProgramFactory.eINSTANCE.createPattern
    val slot = ProcGenUtilities.conditionalSlot(statement.reference.name, Condition.EQUALS)

    if (statement.assignment.value.name !== null && context._functionTable.isFunction(statement.assignment.value.name))
      return Optional.empty

    if ("null".equals(statement.assignment.value.name) || statement.assignment.value.string !== null ||
      statement.assignment.value.number !== null || statement.assignment.value.boolean !== null) {
      slot.value = EcoreUtil.copy(statement.assignment.value)

    } else
      slot.value.name = "null"

    bufferPattern.pattern.slots.add(slot)

    Optional.of(bufferPattern)
  }
}
