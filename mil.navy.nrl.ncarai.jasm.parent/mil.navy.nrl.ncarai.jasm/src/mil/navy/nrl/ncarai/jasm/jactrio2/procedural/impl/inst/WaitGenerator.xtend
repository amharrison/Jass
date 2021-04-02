package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.WaitStatement
import org.jactr.io2.jactr.modelFragment.Condition
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class WaitGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern finalGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof WaitStatement) {

      buildNoMappingProduction(currentGoal, finalGoal, inst, context)

      if (inst.handler !== null) {
        context.consumer.accept(buildRequestFailedProduction(currentGoal, finalGoal, inst, context))
      }
    }
  }

  def buildRequestFailedProduction(Pattern currentGoal, Pattern nextGoal, WaitStatement request,
    ProceduralGenerationContext context) {
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    val pName = context._goalStateProvider.currentFunction.name + "-wait-buffer-error-" + request.buffer.name +
      "-Line" + ProcGenUtilities.lineNumber(request)
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

  def buildNoMappingProduction(Pattern currentGoal, Pattern nextGoal, WaitStatement request,
    ProceduralGenerationContext context) {

    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    val pName = context._goalStateProvider.currentFunction.name + "-wait-buffer-free-" + request.buffer.name + "-Line" +
      ProcGenUtilities.lineNumber(request)
    rtn.name = pName
    val goalBuffer = context._resolver.resolveBuffer("goal")
    val match = ProcGenUtilities.toMatch(currentGoal, context)
    match.name = goalBuffer
    rtn.conditions.add(match)

    val requestVerification = ModelFragmentFactory.eINSTANCE.createQuery
    requestVerification.name = request.buffer
    var slot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS)
    slot.value.name = "free"
    requestVerification.slots.add(slot)
    rtn.conditions.add(requestVerification)

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = goalBuffer
    rtn.actions.add(modify)

    context.consumer.accept(rtn)

    currentGoal
  }

  override handles(Instruction instr) {
    return instr instanceof WaitStatement
  }

  

}
