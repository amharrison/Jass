package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.WhileDoStatement
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class WhileDoGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof WhileDoStatement) {

      context._goalStateProvider.startBranch(inst.block.instructions,currentGoal.slots.last.value.name, context, "reset")
      // context._goalStateProvider.nextState
      var interimGoal = context._goalStateProvider.nextGoalState
      // create and accept the repeat until launcher
      context.consumer.accept(createInitialProduction(currentGoal, interimGoal, inst, context))

      context._productionSequencer.sequence(inst.block.instructions, context)
      val terminalGoal = context._goalStateProvider.currentGoalState

      context.consumer.accept(createFallthroughProduction(currentGoal, nextGoal, inst, context))

      context._goalStateProvider.nextState
      val lastGoal = context._goalStateProvider.currentGoalState

      context.consumer.accept(createLoopBackProduction(lastGoal, currentGoal, inst, context))
      
      context._goalStateProvider.endBranch
    }
  }

  def createFallthroughProduction(Pattern currentGoal, Pattern targetGoal, WhileDoStatement until,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-exit-Line" + ProcGenUtilities.lineNumber(until)
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

  def createLoopBackProduction(Pattern currentGoal, Pattern nextGoal, WhileDoStatement until,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-reset-Line" + ProcGenUtilities.lineNumber(until)
    val goalBuffer = context._resolver.resolveBuffer("goal")
    val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
    goalCondition.name = goalBuffer
    rtn.conditions.add(goalCondition)

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = goalBuffer
    rtn.actions.add(modify)

    rtn
  }

  def createInitialProduction(Pattern currentGoal, Pattern nextGoal, WhileDoStatement whileStatement,
    ProceduralGenerationContext context) {

    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-start-Line" + ProcGenUtilities.lineNumber(whileStatement)

    val goalBuffer = context._resolver.resolveBuffer("goal")

    val conditions = ProcGenUtilities.mergePatterns(currentGoal, whileStatement.^while.bindings.bindings, context)
    // handle goal first
    rtn.conditions.add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context))

    conditions.values.forEach [ bPattern |
      rtn.conditions.add(ProcGenUtilities.toMatch(bPattern, context))
    ]

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = context._resolver.resolveBuffer("goal")
    rtn.actions.add(modify)

    // and any touches we might need
    conditions.keySet.forEach [ buffer |
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = buffer
      rtn.actions.add(touch)
    ]

    rtn
  }

  def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-while"
  }

  override handles(Instruction instr) {
    instr instanceof WhileDoStatement
  }

}
