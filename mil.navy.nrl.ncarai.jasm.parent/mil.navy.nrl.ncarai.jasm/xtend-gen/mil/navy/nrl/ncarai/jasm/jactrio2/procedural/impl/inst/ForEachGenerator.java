package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import java.util.Map;
import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.ForLoopStatement;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.modelFragment.Add;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Condition;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.IsaBlock;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Query;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class ForEachGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof ForLoopStatement)) {
      context._goalStateProvider.startBranch(IterableExtensions.<ConditionalSlot>last(currentGoal.getSlots()).getValue().getName(), context, "requesting", "executing");
      final Pattern requestingState = context._goalStateProvider.nextGoalState();
      context.getConsumer().accept(this.createInitialRequestingProduction(currentGoal, requestingState, ((ForLoopStatement)inst), context));
      context.getConsumer().accept(this.createErrorProduction(requestingState, nextGoal, ((ForLoopStatement)inst), context));
      context._goalStateProvider.nextState();
      final Pattern executingState = context._goalStateProvider.nextGoalState();
      this.createResultProduction(requestingState, executingState, ((ForLoopStatement)inst), context);
      context._goalStateProvider.nextState();
      context.getConsumer().accept(this.createSubsequentRequestingProduction(executingState, requestingState, ((ForLoopStatement)inst), context));
      context._goalStateProvider.endBranch();
    }
  }
  
  public Production createSubsequentRequestingProduction(final Pattern currentGoal, final Pattern nextGoal, final ForLoopStatement loop, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(loop, context);
      String _plus = (_computeBaseName + "-subsequentRequest-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(loop);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      production.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Map<Buffer, BufferPattern> conditions = ProcGenUtilities.mergePatterns(currentGoal, loop.getSubsequentBindings().getBindings(), context);
      production.getConditions().add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context));
      final Consumer<BufferPattern> _function = (BufferPattern bPattern) -> {
        production.getConditions().add(ProcGenUtilities.toMatch(bPattern, context));
      };
      conditions.values().forEach(_function);
      final Query emptyNotBusy = ModelFragmentFactory.eINSTANCE.createQuery();
      emptyNotBusy.setName(loop.getForBuffer());
      ConditionalSlot slot = ProcGenUtilities.conditionalSlot("state", Condition.NOT);
      Value _value = slot.getValue();
      _value.setName("busy");
      emptyNotBusy.getSlots().add(slot);
      production.getConditions().add(emptyNotBusy);
      final Add addRequest = ProcGenUtilities.toAdd(loop.getSubsequent(), context);
      addRequest.setName(loop.getForBuffer());
      production.getActions().add(addRequest);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      production.getActions().add(modify);
      final Consumer<Buffer> _function_1 = (Buffer buffer) -> {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(buffer);
        production.getActions().add(touch);
      };
      conditions.keySet().forEach(_function_1);
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public Production createResultProduction(final Pattern currentGoal, final Pattern nextGoal, final ForLoopStatement loop, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(loop, context);
      String _plus = (_computeBaseName + "-execute-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(loop);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      production.setName(_plus_1);
      final Match goal = ProcGenUtilities.toMatch(currentGoal, context);
      goal.setName(context._resolver.resolveBuffer("goal"));
      production.getConditions().add(goal);
      final Match requested = ModelFragmentFactory.eINSTANCE.createMatch();
      requested.setName(loop.getForBuffer());
      requested.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
      IsaBlock _isa = requested.getIsa();
      String _name = loop.getForBuffer().getName();
      String _plus_2 = ("=" + _name);
      _isa.setName(_plus_2);
      production.getConditions().add(requested);
      context.getConsumer().accept(production);
      final ChunkType chunkType = context._resolver.resolveChunkType(loop.getInitial().getType());
      context._bufferStateManager.setContentType(loop.getForBuffer().getName(), chunkType);
      context._goalStateProvider.startBranch(loop.getBlock().getInstructions(), context);
      Pattern ngp = context._goalStateProvider.nextGoalState();
      context._productionSequencer.sequence(loop.getBlock().getInstructions(), context);
      context._goalStateProvider.endBranch();
      context._bufferStateManager.setContentType(loop.getForBuffer().getName(), null);
      final Modify modify = ProcGenUtilities.toModify(ngp, context);
      modify.setName(goal.getName());
      production.getActions().add(modify);
      final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
      touch.setName(loop.getForBuffer());
      production.getActions().add(touch);
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public Production createErrorProduction(final Pattern currentGoal, final Pattern nextGoal, final ForLoopStatement loop, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(loop, context);
      String _plus = (_computeBaseName + "-error-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(loop);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      production.setName(_plus_1);
      final Match goal = ProcGenUtilities.toMatch(currentGoal, context);
      goal.setName(context._resolver.resolveBuffer("goal"));
      production.getConditions().add(goal);
      final Query error = ModelFragmentFactory.eINSTANCE.createQuery();
      error.setName(loop.getForBuffer());
      ConditionalSlot slot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS);
      Value _value = slot.getValue();
      _value.setName("error");
      error.getSlots().add(slot);
      production.getConditions().add(error);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(goal.getName());
      production.getActions().add(modify);
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public Production createInitialRequestingProduction(final Pattern currentGoal, final Pattern nextGoal, final ForLoopStatement loop, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(loop, context);
      String _plus = (_computeBaseName + "-initialRequest-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(loop);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      production.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Map<Buffer, BufferPattern> conditions = ProcGenUtilities.mergePatterns(currentGoal, loop.getInitialBindings().getBindings(), context);
      production.getConditions().add(ProcGenUtilities.toMatch(conditions.remove(goalBuffer), context));
      final Consumer<BufferPattern> _function = (BufferPattern bPattern) -> {
        production.getConditions().add(ProcGenUtilities.toMatch(bPattern, context));
      };
      conditions.values().forEach(_function);
      final Query emptyNotBusy = ModelFragmentFactory.eINSTANCE.createQuery();
      emptyNotBusy.setName(loop.getForBuffer());
      ConditionalSlot slot = ProcGenUtilities.conditionalSlot("state", Condition.NOT);
      Value _value = slot.getValue();
      _value.setName("busy");
      emptyNotBusy.getSlots().add(slot);
      boolean _isForced = loop.isForced();
      boolean _not = (!_isForced);
      if (_not) {
        slot = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS);
        Value _value_1 = slot.getValue();
        _value_1.setName("empty");
        emptyNotBusy.getSlots().add(slot);
      }
      production.getConditions().add(emptyNotBusy);
      final Add addRequest = ProcGenUtilities.toAdd(loop.getInitial(), context);
      addRequest.setName(loop.getForBuffer());
      production.getActions().add(addRequest);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(context._resolver.resolveBuffer("goal"));
      production.getActions().add(modify);
      final Consumer<Buffer> _function_1 = (Buffer buffer) -> {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(buffer);
        production.getActions().add(touch);
      };
      conditions.keySet().forEach(_function_1);
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public String computeBaseName(final ForLoopStatement loop, final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      String _name = loop.getForBuffer().getName();
      _xblockexpression = ((functionName + "-for-") + _name);
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof ForLoopStatement);
  }
}
