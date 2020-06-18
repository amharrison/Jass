package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.RewardStatement;
import org.jactr.core.production.six.ISubsymbolicProduction6;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Parameter;
import org.jactr.io2.jactr.modelFragment.ParametersBlock;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Value;

/**
 * creates a reward production to propogate the reward signal
 */
@SuppressWarnings("all")
public class RewardGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof RewardStatement)) {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Match goalCondition = ProcGenUtilities.toMatch(currentGoal, context);
      goalCondition.setName(goalBuffer);
      rtn.getConditions().add(goalCondition);
      final Modify goalAction = ProcGenUtilities.toModify(nextGoal, context);
      goalAction.setName(goalBuffer);
      rtn.getActions().add(goalAction);
      final ParametersBlock parameters = ModelFragmentFactory.eINSTANCE.createParametersBlock();
      final Parameter rewardParam = ModelFragmentFactory.eINSTANCE.createParameter();
      rewardParam.setName(ISubsymbolicProduction6.REWARD_PARAM);
      rewardParam.setValue(ModelFragmentFactory.eINSTANCE.createValue());
      Value _value = rewardParam.getValue();
      _value.setNumber(((RewardStatement)inst).getReward());
      parameters.getParameter().add(rewardParam);
      rtn.setParameters(parameters);
      context.getConsumer().accept(rtn);
    }
  }
  
  @Override
  public boolean handles(final Instruction inst) {
    return (inst instanceof RewardStatement);
  }
  
  public String computeBaseName(final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-reward");
    }
    return _xblockexpression;
  }
}
