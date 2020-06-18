package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.RemoveStatement;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.IsaBlock;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Remove;

@SuppressWarnings("all")
public class RemoveGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof RemoveStatement)) {
      context.getConsumer().accept(this.createRemoveProduction(currentGoal, nextGoal, ((RemoveStatement)inst), context));
    }
  }
  
  public Production createRemoveProduction(final Pattern currentGoal, final Pattern nextGoal, final RemoveStatement inst, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_1);
      final Match goalCondition = ProcGenUtilities.toMatch(currentGoal, context);
      goalCondition.setName(context._resolver.resolveBuffer("goal"));
      rtn.getConditions().add(goalCondition);
      final Consumer<Buffer> _function = (Buffer buffer) -> {
        final Match match = ModelFragmentFactory.eINSTANCE.createMatch();
        match.setName(buffer);
        match.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
        IsaBlock _isa = match.getIsa();
        String _name = buffer.getName();
        String _plus_2 = ("=" + _name);
        _isa.setName(_plus_2);
        rtn.getConditions().add(match);
      };
      inst.getBuffers().forEach(_function);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(goalCondition.getName());
      rtn.getActions().add(modify);
      final Consumer<Buffer> _function_1 = (Buffer buffer) -> {
        final Remove remove = ModelFragmentFactory.eINSTANCE.createRemove();
        remove.setName(buffer);
        rtn.getActions().add(remove);
      };
      inst.getBuffers().forEach(_function_1);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public String computeBaseName(final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-remove");
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof RemoveStatement);
  }
}
