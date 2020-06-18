package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.DoWhileStatement
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class DoWhileGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof DoWhileStatement) {

      /*
       * handle the block. Not sure why we don't call nextState, but it works.
       */
      val instructions = newArrayList
      instructions.addAll(inst.block.instructions)
      instructions.add(inst.^while)
      context._goalStateProvider.startBranch(instructions, context)
      // context._goalStateProvider.nextState
      var interimGoal = context._goalStateProvider.nextGoalState
      // create and accept the repeat until launcher
      context.consumer.accept(createInitialRepeatProduction(currentGoal, interimGoal, inst, context))

      context._productionSequencer.sequence(inst.block.instructions, context)

      context._goalStateProvider.nextState

      interimGoal = context._goalStateProvider.currentGoalState
      
      context.consumer.accept(
        createTerminalRepeatProduction(interimGoal, currentGoal, inst, context))
      context.consumer.accept(
        createFallthroughRepeatProduction(interimGoal, nextGoal, inst, context))

      context._goalStateProvider.endBranch
    }
  }

  def createFallthroughRepeatProduction(Pattern currentGoal, Pattern targetGoal, DoWhileStatement until,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-reset-Line" + ProcGenUtilities.lineNumber(until)
    val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
    goalCondition.name = context._resolver.resolveBuffer("goal")
    rtn.conditions.add(goalCondition)

    val modify = ProcGenUtilities.toModify(targetGoal, context)
    modify.name = context._resolver.resolveBuffer("goal")
    rtn.actions.add(modify)

    rtn.parameters = ModelFragmentFactory.eINSTANCE.createParametersBlock
    var p = ModelFragmentFactory.eINSTANCE.createParameter
    p.name = context._prioritizer.priorityParameterName
    p.value = ModelFragmentFactory.eINSTANCE.createValue
    p.value.string = context._prioritizer.fallbackPriority
    rtn.parameters.parameter.add(p)

    rtn

  }

  def createTerminalRepeatProduction(Pattern currentGoal, Pattern nextGoal, DoWhileStatement until,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-terminate-Line" + ProcGenUtilities.lineNumber(until)
    val goalBuffer = context._resolver.resolveBuffer("goal")

    val conditions = ProcGenUtilities.mergePatterns(currentGoal, until.^while.bindings.bindings, context)
    // handle goal first
    rtn.conditions.add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context))

    conditions.values.forEach [ bPattern |
      rtn.conditions.add(ProcGenUtilities.toMatch(bPattern, context))
    ]

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = goalBuffer
    rtn.actions.add(modify)

    // and any touches we might need
    conditions.keySet.forEach [ buffer |
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = buffer
      rtn.actions.add(touch)
    ]

    rtn
  }

  def createInitialRepeatProduction(Pattern currentGoal, Pattern nextGoal, DoWhileStatement until,
    ProceduralGenerationContext context) {

    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-start-Line" + ProcGenUtilities.lineNumber(until)
    val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
    goalCondition.name = context._resolver.resolveBuffer("goal")
    rtn.conditions.add(goalCondition)

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = context._resolver.resolveBuffer("goal")
    rtn.actions.add(modify)

    rtn
  }

  def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-dowhile"
  }

  override handles(Instruction instr) {
    instr instanceof DoWhileStatement
  }

}
