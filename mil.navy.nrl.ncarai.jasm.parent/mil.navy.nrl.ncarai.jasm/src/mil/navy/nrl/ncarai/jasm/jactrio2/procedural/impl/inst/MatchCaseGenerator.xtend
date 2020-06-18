package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.CaseStatement
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.MatchStatement
import mil.navy.nrl.ncarai.jasm.program.Pattern
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class MatchCaseGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof MatchStatement) {
      inst.cases.forEach [ matchCase |
        context.consumer.accept(buildCaseProduction(currentGoal, nextGoal, inst, matchCase, context))
      ]

      context.consumer.accept(buildDefaultProduction(currentGoal, nextGoal, inst, context))
    }
  }

  def buildCaseProduction(Pattern currentGoal, Pattern endGoal, MatchStatement statement, CaseStatement matchCase,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = computeBaseName(statement, context) + "-case-Line" + ProcGenUtilities.lineNumber(matchCase)
    val goalBuffer = context._resolver.resolveBuffer("goal")
    
    val conditions = ProcGenUtilities.mergePatterns(currentGoal, matchCase.bindings.bindings, context)
    // handle goal first
    production.conditions.add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context))

    conditions.values.forEach [ bPattern |
      production.conditions.add(ProcGenUtilities.toMatch(bPattern, context))
    ]

    context._goalStateProvider.startBranch(matchCase.block.instructions, context)
    val ngp = context._goalStateProvider.nextGoalState

    val modify = ProcGenUtilities.toModify(ngp, context)
    modify.name = context._resolver.resolveBuffer("goal")
    production.actions.add(modify)

    // and any touches we might need
    conditions.keySet.forEach [ buffer |
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = buffer
      production.actions.add(touch)
    ]

    context._productionSequencer.sequence(matchCase.block.instructions, context)

    context._goalStateProvider.endBranch()
    
    /*
     * custom priority
     */
    if (matchCase.priority !== null) {
      production.parameters = ModelFragmentFactory.eINSTANCE.createParametersBlock
      var p = ModelFragmentFactory.eINSTANCE.createParameter
      p.name = context._prioritizer.priorityParameterName
      p.value = ModelFragmentFactory.eINSTANCE.createValue
      p.value.string = matchCase.priority.toString
      production.parameters.parameter.add(p)
    }

    production
  }

  def buildDefaultProduction(Pattern currentGoal, Pattern endGoal, MatchStatement statement,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = computeBaseName(statement, context) + "-default-Line" +
      ProcGenUtilities.lineNumber(statement.defaultBlock)
    val goalBuffer = context._resolver.resolveBuffer("goal")

    val goal = ProcGenUtilities.toMatch(currentGoal, context)
    goal.name = goalBuffer
    production.conditions.add(goal)

    context._goalStateProvider.startBranch(statement.defaultBlock.instructions, context)
    val ngp = context._goalStateProvider.nextGoalState
    context._productionSequencer.sequence(statement.defaultBlock.instructions, context)
    context._goalStateProvider.endBranch()
    
    val modify = ProcGenUtilities.toModify(ngp, context)
    modify.name = goalBuffer
    production.actions.add(modify)

    /*
     * default fall through priortiy
     */
    production.parameters = ModelFragmentFactory.eINSTANCE.createParametersBlock
    var p = ModelFragmentFactory.eINSTANCE.createParameter
    p.name = context._prioritizer.priorityParameterName
    p.value = ModelFragmentFactory.eINSTANCE.createValue
    p.value.string = context._prioritizer.fallbackPriority
    production.parameters.parameter.add(p)

    production
  }

  def computeBaseName(MatchStatement loop, ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-match"
  }

  override handles(Instruction inst) {
    inst instanceof MatchStatement
  }

}
