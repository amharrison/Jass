package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import java.util.Map;
import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.CaseStatement;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.MatchStatement;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Parameter;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class MatchCaseGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof MatchStatement)) {
      final Consumer<CaseStatement> _function = (CaseStatement matchCase) -> {
        context.getConsumer().accept(this.buildCaseProduction(currentGoal, nextGoal, ((MatchStatement)inst), matchCase, context));
      };
      ((MatchStatement)inst).getCases().forEach(_function);
      context.getConsumer().accept(this.buildDefaultProduction(currentGoal, nextGoal, ((MatchStatement)inst), context));
    }
  }
  
  public Production buildCaseProduction(final Pattern currentGoal, final Pattern endGoal, final MatchStatement statement, final CaseStatement matchCase, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(statement, context);
      String _plus = (_computeBaseName + "-case-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(matchCase);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      production.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Map<Buffer, BufferPattern> conditions = ProcGenUtilities.mergePatterns(currentGoal, matchCase.getBindings().getBindings(), context);
      production.getConditions().add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context));
      final Consumer<BufferPattern> _function = (BufferPattern bPattern) -> {
        production.getConditions().add(ProcGenUtilities.toMatch(bPattern, context));
      };
      conditions.values().forEach(_function);
      context._goalStateProvider.startBranch(matchCase.getBlock().getInstructions(), context);
      final Pattern ngp = context._goalStateProvider.nextGoalState();
      final Modify modify = ProcGenUtilities.toModify(ngp, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      production.getActions().add(modify);
      final Consumer<Buffer> _function_1 = (Buffer buffer) -> {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(buffer);
        production.getActions().add(touch);
      };
      conditions.keySet().forEach(_function_1);
      context._productionSequencer.sequence(matchCase.getBlock().getInstructions(), context);
      context._goalStateProvider.endBranch();
      Double _priority = matchCase.getPriority();
      boolean _tripleNotEquals = (_priority != null);
      if (_tripleNotEquals) {
        production.setParameters(ModelFragmentFactory.eINSTANCE.createParametersBlock());
        Parameter p = ModelFragmentFactory.eINSTANCE.createParameter();
        p.setName(context._prioritizer.getPriorityParameterName());
        p.setValue(ModelFragmentFactory.eINSTANCE.createValue());
        Value _value = p.getValue();
        _value.setString(matchCase.getPriority().toString());
        production.getParameters().getParameter().add(p);
      }
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public Production buildDefaultProduction(final Pattern currentGoal, final Pattern endGoal, final MatchStatement statement, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(statement, context);
      String _plus = (_computeBaseName + "-default-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(statement.getDefaultBlock());
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      production.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Match goal = ProcGenUtilities.toMatch(currentGoal, context);
      goal.setName(goalBuffer);
      production.getConditions().add(goal);
      context._goalStateProvider.startBranch(statement.getDefaultBlock().getInstructions(), context);
      final Pattern ngp = context._goalStateProvider.nextGoalState();
      context._productionSequencer.sequence(statement.getDefaultBlock().getInstructions(), context);
      context._goalStateProvider.endBranch();
      final Modify modify = ProcGenUtilities.toModify(ngp, context);
      modify.setName(goalBuffer);
      production.getActions().add(modify);
      production.setParameters(ModelFragmentFactory.eINSTANCE.createParametersBlock());
      Parameter p = ModelFragmentFactory.eINSTANCE.createParameter();
      p.setName(context._prioritizer.getPriorityParameterName());
      p.setValue(ModelFragmentFactory.eINSTANCE.createValue());
      Value _value = p.getValue();
      _value.setString(context._prioritizer.getFallbackPriority());
      production.getParameters().getParameter().add(p);
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public String computeBaseName(final MatchStatement loop, final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-match");
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction inst) {
    return (inst instanceof MatchStatement);
  }
}
