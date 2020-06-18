package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.IfThenElseStatement
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class IfThenElseGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof IfThenElseStatement) {
      val baseName = computeBaseName(context)
      /*
       * we create the test production as the combination of the currentGoal and 
       * the condition, and a fail through to handle the else. 
       */
      val testProduction = buildTestProduction(currentGoal, nextGoal, inst, context)
      testProduction.name = baseName + "-test-Line" + ProcGenUtilities.lineNumber(inst)

      // already accepted in buildTest
      // context.consumer.accept(testProduction)
      val fallThroughProduction = buildFallThroughProduction(currentGoal, nextGoal, inst, context)
      fallThroughProduction.name = baseName + "-fail-Line" + ProcGenUtilities.lineNumber(inst)
    // already accepeted in buildFallThrough
    // context.consumer.accept(fallThroughProduction)
    }
  }

  def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-ite"
  }

  def buildFallThroughProduction(Pattern currentGoalPattern, Pattern nextGoalPattern, IfThenElseStatement ite,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    val goalCondition = ProcGenUtilities.toMatch(currentGoalPattern, context)
    goalCondition.name = context._resolver.resolveBuffer("goal")
    rtn.conditions.add(goalCondition)

    rtn.parameters = ModelFragmentFactory.eINSTANCE.createParametersBlock
    var p = ModelFragmentFactory.eINSTANCE.createParameter
    p.name = context._prioritizer.priorityParameterName
    p.value = ModelFragmentFactory.eINSTANCE.createValue
    p.value.string = context._prioritizer.fallbackPriority
    rtn.parameters.parameter.add(p)
    
    // premature consume to place before block in output
    context.consumer.accept(rtn)

    var ngp = nextGoalPattern
    if (ite.elseBlock !== null) {
      context._goalStateProvider.startBranch(ite.elseBlock.instructions, context)
      ngp = context._goalStateProvider.nextGoalState
      context._productionSequencer.sequence(ite.elseBlock.instructions, context)
      context._goalStateProvider.endBranch()
    }

    val modify = ProcGenUtilities.toModify(ngp, context)
    modify.name = context._resolver.resolveBuffer("goal")
    rtn.actions.add(modify)

    rtn
  }

  def buildTestProduction(Pattern currentGoalPattern, Pattern nextGoalPattern, IfThenElseStatement ite,
    ProceduralGenerationContext context) {
    /*
     * condition
     */
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    val goalBuffer = context._resolver.resolveBuffer("goal")

    val conditions = ProcGenUtilities.mergePatterns(currentGoalPattern, ite.bindings.bindings, context)
    // handle goal first
    rtn.conditions.add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context))

    conditions.values.forEach [ bPattern |
      rtn.conditions.add(ProcGenUtilities.toMatch(bPattern, context))
    ]

    // premature consume to place before block in output
    context.consumer.accept(rtn)

    /*
     * action
     */
    context._goalStateProvider.startBranch(ite.thenBlock.instructions, context)
    val ngp = context._goalStateProvider.nextGoalState

    val modify = ProcGenUtilities.toModify(ngp, context)
    modify.name = context._resolver.resolveBuffer("goal")
    rtn.actions.add(modify)

    // and any touches we might need
    conditions.keySet.forEach [ buffer |
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = buffer
      rtn.actions.add(touch)
    ]

    context._productionSequencer.sequence(ite.thenBlock.instructions, context)

    context._goalStateProvider.endBranch()

    rtn
  }

  override handles(Instruction instr) {
    return instr instanceof IfThenElseStatement
  }

}
