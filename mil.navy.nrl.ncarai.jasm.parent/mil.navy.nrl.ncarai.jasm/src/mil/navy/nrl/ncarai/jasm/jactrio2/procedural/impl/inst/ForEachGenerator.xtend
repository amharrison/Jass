package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import org.jactr.io2.jactr.modelFragment.Condition
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class ForEachGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof ForLoopStatement) {
      /*
       * initial production to launch initial request (and null parameter)
       * error production to stop for loop
       * result -> block 
       * goal return -> subsequent request
       */
      context._goalStateProvider.startBranch(currentGoal.slots.last.value.name, context, "requesting", "executing")
      val requestingState = context._goalStateProvider.nextGoalState

      context.consumer.accept(createInitialRequestingProduction(currentGoal, requestingState, inst, context))

      context.consumer.accept(createErrorProduction(requestingState, nextGoal, inst, context))

      context._goalStateProvider.nextState
      val executingState = context._goalStateProvider.nextGoalState

      // will consume itself for better ordering
      createResultProduction(requestingState, executingState, inst, context)

      context._goalStateProvider.nextState // we still call next state to keep internal structures up to date
      context.consumer.accept(createSubsequentRequestingProduction(executingState, requestingState, inst, context))

      context._goalStateProvider.endBranch
    }
  }

  def createSubsequentRequestingProduction(Pattern currentGoal, Pattern nextGoal, ForLoopStatement loop,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = computeBaseName(loop, context) + "-subsequentRequest-Line" + ProcGenUtilities.lineNumber(loop)

    val goalBuffer = context._resolver.resolveBuffer("goal")

    val conditions = ProcGenUtilities.mergePatterns(currentGoal, loop.subsequentBindings.bindings, context)
    // handle goal first
    production.conditions.add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context))

    conditions.values.forEach [ bPattern |
      production.conditions.add(ProcGenUtilities.toMatch(bPattern, context))
    ]

    val emptyNotBusy = ModelFragmentFactory.eINSTANCE.createQuery
    emptyNotBusy.name = loop.forBuffer
    var slot = ProcGenUtilities.conditionalSlot("state", Condition.NOT)
    slot.value.name = "busy"
    emptyNotBusy.slots.add(slot)
// can't be empty 
//    slot = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS)
//    slot.value.name = "empty"
//    emptyNotBusy.slots.add(slot)
    production.conditions.add(emptyNotBusy)

    val addRequest = ProcGenUtilities.toAdd(loop.subsequent, context)
    addRequest.name = loop.forBuffer
    production.actions.add(addRequest)

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = context._resolver.resolveBuffer("goal")
    production.actions.add(modify)

    conditions.keySet.forEach [ buffer |
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = buffer
      production.actions.add(touch)
    ]

    production
  }

  def createResultProduction(Pattern currentGoal, Pattern nextGoal, ForLoopStatement loop,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = computeBaseName(loop, context) + "-execute-Line" + ProcGenUtilities.lineNumber(loop)

    val goal = ProcGenUtilities.toMatch(currentGoal, context)
    goal.name = context._resolver.resolveBuffer("goal")
    production.conditions.add(goal)

    val requested = ModelFragmentFactory.eINSTANCE.createMatch
    requested.name = loop.forBuffer
    requested.isa = ModelFragmentFactory.eINSTANCE.createIsaBlock
    requested.isa.name = "=" + loop.forBuffer.name
    production.conditions.add(requested)

    // premature consume so that this production preceeds the block
    context.consumer.accept(production)

    /*
     * infer the chunktype of the request
     */
    val chunkType = context._resolver.resolveChunkType(loop.initial.type)
    context._bufferStateManager.setContentType(loop.forBuffer.name, chunkType)

    context._goalStateProvider.startBranch(loop.block.instructions, context)
    var ngp = context._goalStateProvider.nextGoalState
    context._productionSequencer.sequence(loop.block.instructions, context)
    context._goalStateProvider.endBranch

    context._bufferStateManager.setContentType(loop.forBuffer.name, null)

    val modify = ProcGenUtilities.toModify(ngp, context)
    modify.name = goal.name
    production.actions.add(modify)

    // touch request buffer just in case
    val touch = ModelFragmentFactory.eINSTANCE.createModify
    touch.name = loop.forBuffer
    production.actions.add(touch)

    production
  }

  def createErrorProduction(Pattern currentGoal, Pattern nextGoal, ForLoopStatement loop,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = computeBaseName(loop, context) + "-error-Line" + ProcGenUtilities.lineNumber(loop)

    val goal = ProcGenUtilities.toMatch(currentGoal, context)
    goal.name = context._resolver.resolveBuffer("goal")
    production.conditions.add(goal)

    val error = ModelFragmentFactory.eINSTANCE.createQuery
    error.name = loop.forBuffer
    var slot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS)
    slot.value.name = "error"
    error.slots.add(slot)

    production.conditions.add(error)

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = goal.name
    production.actions.add(modify)

    production
  }

  def createInitialRequestingProduction(Pattern currentGoal, Pattern nextGoal, ForLoopStatement loop,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = computeBaseName(loop, context) + "-initialRequest-Line" + ProcGenUtilities.lineNumber(loop)

    val goalBuffer = context._resolver.resolveBuffer("goal")

    val conditions = ProcGenUtilities.mergePatterns(currentGoal, loop.initialBindings.bindings, context)
    // handle goal first
    production.conditions.add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context))

    conditions.values.forEach [ bPattern |
      production.conditions.add(ProcGenUtilities.toMatch(bPattern, context))
    ]

    val emptyNotBusy = ModelFragmentFactory.eINSTANCE.createQuery
    emptyNotBusy.name = loop.forBuffer
    var slot = ProcGenUtilities.conditionalSlot("state", Condition.NOT)
    slot.value.name = "busy"
    emptyNotBusy.slots.add(slot)

    if (!loop.forced) {
      slot = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS)
      slot.value.name = "empty"
      emptyNotBusy.slots.add(slot)
    }

    production.conditions.add(emptyNotBusy)

    val addRequest = ProcGenUtilities.toAdd(loop.initial, context)
    addRequest.name = loop.forBuffer
    production.actions.add(addRequest)

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = context._resolver.resolveBuffer("goal")
    production.actions.add(modify)

    conditions.keySet.forEach [ buffer |
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = buffer
      production.actions.add(touch)
    ]

    production
  }

  def computeBaseName(ForLoopStatement loop, ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-for-" + loop.forBuffer.name
  }

  override handles(Instruction instr) {
    instr instanceof ForLoopStatement
  }

}
