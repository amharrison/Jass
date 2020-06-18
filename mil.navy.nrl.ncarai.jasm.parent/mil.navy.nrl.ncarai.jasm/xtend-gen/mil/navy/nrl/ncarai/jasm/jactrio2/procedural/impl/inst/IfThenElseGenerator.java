package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import java.util.Map;
import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.Block;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.IfThenElseStatement;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Parameter;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class IfThenElseGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof IfThenElseStatement)) {
      final String baseName = this.computeBaseName(context);
      final Production testProduction = this.buildTestProduction(currentGoal, nextGoal, ((IfThenElseStatement)inst), context);
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus = ((baseName + "-test-Line") + Integer.valueOf(_lineNumber));
      testProduction.setName(_plus);
      final Production fallThroughProduction = this.buildFallThroughProduction(currentGoal, nextGoal, ((IfThenElseStatement)inst), context);
      int _lineNumber_1 = ProcGenUtilities.lineNumber(inst);
      String _plus_1 = ((baseName + "-fail-Line") + Integer.valueOf(_lineNumber_1));
      fallThroughProduction.setName(_plus_1);
    }
  }
  
  public String computeBaseName(final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-ite");
    }
    return _xblockexpression;
  }
  
  public Production buildFallThroughProduction(final Pattern currentGoalPattern, final Pattern nextGoalPattern, final IfThenElseStatement ite, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      final Match goalCondition = ProcGenUtilities.toMatch(currentGoalPattern, context);
      goalCondition.setName(context._resolver.resolveBuffer("goal"));
      rtn.getConditions().add(goalCondition);
      rtn.setParameters(ModelFragmentFactory.eINSTANCE.createParametersBlock());
      Parameter p = ModelFragmentFactory.eINSTANCE.createParameter();
      p.setName(context._prioritizer.getPriorityParameterName());
      p.setValue(ModelFragmentFactory.eINSTANCE.createValue());
      Value _value = p.getValue();
      _value.setString(context._prioritizer.getFallbackPriority());
      rtn.getParameters().getParameter().add(p);
      context.getConsumer().accept(rtn);
      Pattern ngp = nextGoalPattern;
      Block _elseBlock = ite.getElseBlock();
      boolean _tripleNotEquals = (_elseBlock != null);
      if (_tripleNotEquals) {
        context._goalStateProvider.startBranch(ite.getElseBlock().getInstructions(), context);
        ngp = context._goalStateProvider.nextGoalState();
        context._productionSequencer.sequence(ite.getElseBlock().getInstructions(), context);
        context._goalStateProvider.endBranch();
      }
      final Modify modify = ProcGenUtilities.toModify(ngp, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      rtn.getActions().add(modify);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public Production buildTestProduction(final Pattern currentGoalPattern, final Pattern nextGoalPattern, final IfThenElseStatement ite, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Map<Buffer, BufferPattern> conditions = ProcGenUtilities.mergePatterns(currentGoalPattern, ite.getBindings().getBindings(), context);
      rtn.getConditions().add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context));
      final Consumer<BufferPattern> _function = (BufferPattern bPattern) -> {
        rtn.getConditions().add(ProcGenUtilities.toMatch(bPattern, context));
      };
      conditions.values().forEach(_function);
      context.getConsumer().accept(rtn);
      context._goalStateProvider.startBranch(ite.getThenBlock().getInstructions(), context);
      final Pattern ngp = context._goalStateProvider.nextGoalState();
      final Modify modify = ProcGenUtilities.toModify(ngp, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      rtn.getActions().add(modify);
      final Consumer<Buffer> _function_1 = (Buffer buffer) -> {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(buffer);
        rtn.getActions().add(touch);
      };
      conditions.keySet().forEach(_function_1);
      context._productionSequencer.sequence(ite.getThenBlock().getInstructions(), context);
      context._goalStateProvider.endBranch();
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof IfThenElseStatement);
  }
}
