package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.RewardStatement
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory
import org.jactr.core.production.six.ISubsymbolicProduction6

/**
 * creates a reward production to propogate the reward signal
 */
class RewardGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof RewardStatement) {

      val rtn = ModelFragmentFactory.eINSTANCE.createProduction
      rtn.name = computeBaseName(context) + "-Line" + ProcGenUtilities.lineNumber(inst)

      val goalBuffer = context._resolver.resolveBuffer("goal")
      val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
      goalCondition.name = goalBuffer
      rtn.conditions.add(goalCondition)

      val goalAction = ProcGenUtilities.toModify(nextGoal, context)
      goalAction.name = goalBuffer
      rtn.actions.add(goalAction)
      
      /*
       * and the parameter that controls the reward signal
       */
      val parameters = ModelFragmentFactory.eINSTANCE.createParametersBlock
      val rewardParam = ModelFragmentFactory.eINSTANCE.createParameter
      rewardParam.name = ISubsymbolicProduction6.REWARD_PARAM
      rewardParam.value = ModelFragmentFactory.eINSTANCE.createValue
      rewardParam.value.number = inst.reward
      parameters.parameter.add(rewardParam)
      rtn.parameters = parameters
      
      //and consume
      context.consumer.accept(rtn)
    }
  }

  override handles(Instruction inst) {
    inst instanceof RewardStatement
  }

  def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-reward"
  }
}
