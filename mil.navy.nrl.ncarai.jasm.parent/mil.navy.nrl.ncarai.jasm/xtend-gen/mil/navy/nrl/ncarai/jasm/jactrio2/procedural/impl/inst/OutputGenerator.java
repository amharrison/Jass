package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import java.util.Map;
import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.OutputStatement;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Output;
import org.jactr.io2.jactr.modelFragment.Production;

@SuppressWarnings("all")
public class OutputGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof OutputStatement)) {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Map<Buffer, BufferPattern> conditions = ProcGenUtilities.mergePatterns(currentGoal, ((OutputStatement)inst).getBindings().getBindings(), context);
      rtn.getConditions().add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context));
      final Consumer<BufferPattern> _function = (BufferPattern bPattern) -> {
        rtn.getConditions().add(ProcGenUtilities.toMatch(bPattern, context));
      };
      conditions.values().forEach(_function);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(goalBuffer);
      rtn.getActions().add(modify);
      final Output output = ModelFragmentFactory.eINSTANCE.createOutput();
      output.setString(((OutputStatement)inst).getString());
      rtn.getActions().add(output);
      final Consumer<Buffer> _function_1 = (Buffer buffer) -> {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(buffer);
        rtn.getActions().add(touch);
      };
      conditions.keySet().forEach(_function_1);
      context.getConsumer().accept(rtn);
    }
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof OutputStatement);
  }
  
  public String computeBaseName(final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-output");
    }
    return _xblockexpression;
  }
}
