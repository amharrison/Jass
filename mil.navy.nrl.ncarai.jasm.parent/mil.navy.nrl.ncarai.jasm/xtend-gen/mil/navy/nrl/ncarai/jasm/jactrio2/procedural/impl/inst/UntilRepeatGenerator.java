package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import java.util.Map;
import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.UntilRepeatStatement;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Parameter;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class UntilRepeatGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof UntilRepeatStatement)) {
      context._goalStateProvider.startBranch(((UntilRepeatStatement)inst).getBlock().getInstructions(), IterableExtensions.<ConditionalSlot>last(currentGoal.getSlots()).getValue().getName(), context, "reset");
      Pattern interimGoal = context._goalStateProvider.nextGoalState();
      context.getConsumer().accept(this.createInitialProduction(currentGoal, nextGoal, ((UntilRepeatStatement)inst), context));
      context._productionSequencer.sequence(((UntilRepeatStatement)inst).getBlock().getInstructions(), context);
      final Pattern terminalGoal = context._goalStateProvider.currentGoalState();
      context.getConsumer().accept(this.createLoopBackProduction(currentGoal, interimGoal, ((UntilRepeatStatement)inst), context));
      context._goalStateProvider.nextState();
      final Pattern lastGoal = context._goalStateProvider.currentGoalState();
      context.getConsumer().accept(this.createFallthroughProduction(lastGoal, currentGoal, ((UntilRepeatStatement)inst), context));
      context._goalStateProvider.endBranch();
    }
  }
  
  public Production createFallthroughProduction(final Pattern currentGoal, final Pattern targetGoal, final UntilRepeatStatement until, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-reset-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(until);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_1);
      final Match goalCondition = ProcGenUtilities.toMatch(currentGoal, context);
      goalCondition.setName(context._resolver.resolveBuffer("goal"));
      rtn.getConditions().add(goalCondition);
      final Modify modify = ProcGenUtilities.toModify(targetGoal, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      rtn.getActions().add(modify);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public Production createLoopBackProduction(final Pattern currentGoal, final Pattern nextGoal, final UntilRepeatStatement until, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-start-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(until);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Match goalCondition = ProcGenUtilities.toMatch(currentGoal, context);
      goalCondition.setName(goalBuffer);
      rtn.getConditions().add(goalCondition);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(goalBuffer);
      rtn.getActions().add(modify);
      rtn.setParameters(ModelFragmentFactory.eINSTANCE.createParametersBlock());
      Parameter p = ModelFragmentFactory.eINSTANCE.createParameter();
      p.setName(context._prioritizer.getPriorityParameterName());
      p.setValue(ModelFragmentFactory.eINSTANCE.createValue());
      Value _value = p.getValue();
      _value.setString(context._prioritizer.getFallbackPriority());
      rtn.getParameters().getParameter().add(p);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public Production createInitialProduction(final Pattern currentGoal, final Pattern nextGoal, final UntilRepeatStatement untilStatement, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-until-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(untilStatement);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Map<Buffer, BufferPattern> conditions = ProcGenUtilities.mergePatterns(currentGoal, untilStatement.getUntil().getBindings().getBindings(), context);
      rtn.getConditions().add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context));
      final Consumer<BufferPattern> _function = (BufferPattern bPattern) -> {
        rtn.getConditions().add(ProcGenUtilities.toMatch(bPattern, context));
      };
      conditions.values().forEach(_function);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      rtn.getActions().add(modify);
      final Consumer<Buffer> _function_1 = (Buffer buffer) -> {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(buffer);
        rtn.getActions().add(touch);
      };
      conditions.keySet().forEach(_function_1);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public String computeBaseName(final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-until");
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof UntilRepeatStatement);
  }
}
