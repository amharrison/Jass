package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import com.google.common.base.Objects;
import java.util.Optional;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.FunctionalCallGenerator;
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.FunctionCallStatement;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.ProgramFactory;
import mil.navy.nrl.ncarai.jasm.program.VariableStatement;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
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
public class AssignmentGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof AssignmentStatement)) {
      FunctionCallStatement _functionCall = ((AssignmentStatement)inst).getAssignment().getFunctionCall();
      boolean _tripleNotEquals = (_functionCall != null);
      if (_tripleNotEquals) {
        final FunctionCallStatement function = ((AssignmentStatement)inst).getAssignment().getFunctionCall();
        context._goalStateProvider.startBranch(IterableExtensions.<ConditionalSlot>last(currentGoal.getSlots()).getValue().getName(), context, "executing");
        final Pattern executingState = context._goalStateProvider.nextGoalState();
        context.getConsumer().accept(
          FunctionalCallGenerator.createInitialProduction(currentGoal, executingState, function, function.getFunction(), context));
        context._goalStateProvider.nextState();
        boolean _isChunk = function.getFunction().isChunk();
        if (_isChunk) {
          context.getConsumer().accept(
            FunctionalCallGenerator.createTerminalProduction(executingState, nextGoal, inst, function.getFunction(), context, "=return"));
        } else {
          boolean _isVoid = function.getFunction().isVoid();
          if (_isVoid) {
            context.getConsumer().accept(
              FunctionalCallGenerator.createTerminalProduction(executingState, nextGoal, inst, function.getFunction(), context, "null"));
          }
        }
        context._goalStateProvider.endBranch();
      } else {
        context.getConsumer().accept(AssignmentGenerator.createSimpleAssignmentProduction(currentGoal, nextGoal, ((AssignmentStatement)inst), null, context));
        final Production nullProduction = AssignmentGenerator.createSimpleAssignmentNullProduction(currentGoal, nextGoal, ((AssignmentStatement)inst), null, context);
        if ((nullProduction != null)) {
          context.getConsumer().accept(nullProduction);
        }
      }
    }
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof AssignmentStatement);
  }
  
  public static Production createSimpleAssignmentProduction(final Pattern currentGoal, final Pattern nextGoal, final AssignmentStatement inst, final Buffer bufferToQuery, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _name = context._goalStateProvider.currentFunction().getName();
      String _plus = (_name + "-assignment-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      production.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      Pattern cgp = currentGoal;
      Optional<BufferPattern> match = AssignmentGenerator.matchAsBufferPattern(inst, context);
      boolean _isPresent = match.isPresent();
      if (_isPresent) {
        Buffer _buffer = match.get().getBuffer();
        boolean _tripleEquals = (_buffer == goalBuffer);
        if (_tripleEquals) {
          cgp = ProcGenUtilities.merge(cgp, match.get().getPattern(), context);
        }
      }
      final Match goalCondition = ProcGenUtilities.toMatch(cgp, context);
      goalCondition.setName(goalBuffer);
      production.getConditions().add(goalCondition);
      if ((bufferToQuery != null)) {
        final Query query = ModelFragmentFactory.eINSTANCE.createQuery();
        query.setName(bufferToQuery);
        final ConditionalSlot bSlot = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS);
        Value _value = bSlot.getValue();
        _value.setName("full");
        query.getSlots().add(bSlot);
        final ConditionalSlot sSlot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS);
        Value _value_1 = sSlot.getValue();
        _value_1.setName("free");
        query.getSlots().add(sSlot);
        production.getConditions().add(query);
      }
      if (((inst.getReference().getBuffer() != null) && (inst.getReference().getBuffer() != goalBuffer))) {
        final Optional<ChunkType> chunkType = context._bufferStateManager.getContentType(inst.getReference().getBuffer().getName());
        final Match bindingCondition = ModelFragmentFactory.eINSTANCE.createMatch();
        bindingCondition.setName(inst.getReference().getBuffer());
        bindingCondition.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
        boolean _isPresent_1 = chunkType.isPresent();
        if (_isPresent_1) {
          IsaBlock _isa = bindingCondition.getIsa();
          _isa.setType(chunkType.get());
        } else {
          IsaBlock _isa_1 = bindingCondition.getIsa();
          String _name_1 = inst.getReference().getBuffer().getName();
          String _plus_2 = ("=" + _name_1);
          _isa_1.setName(_plus_2);
        }
        production.getConditions().add(bindingCondition);
      }
      if ((match.isPresent() && (match.get().getBuffer() != goalBuffer))) {
        final Match bindingCondition_1 = ProcGenUtilities.toMatch(match.get().getPattern(), context);
        bindingCondition_1.setName(match.get().getBuffer());
        production.getConditions().add(bindingCondition_1);
      }
      Pattern ngp = nextGoal;
      Optional<BufferPattern> modify = AssignmentGenerator.modifyAsBufferPattern(inst, context);
      if ((modify.isPresent() && (modify.get().getBuffer() == goalBuffer))) {
        ngp = ProcGenUtilities.merge(ngp, modify.get().getPattern(), context);
      }
      final Modify goalAction = ProcGenUtilities.toModify(ngp, context);
      goalAction.setName(goalBuffer);
      production.getActions().add(goalAction);
      if ((modify.isPresent() && (modify.get().getBuffer() != goalBuffer))) {
        final Modify bindingCondition_2 = ProcGenUtilities.toModify(modify.get().getPattern(), context);
        bindingCondition_2.setName(modify.get().getBuffer());
        production.getActions().add(bindingCondition_2);
      }
      if (((match.isPresent() && (match.get().getBuffer() != goalBuffer)) && ((!modify.isPresent()) || (modify.get().getBuffer() != match.get().getBuffer())))) {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(match.get().getBuffer());
        production.getActions().add(touch);
      }
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public static Production createSimpleAssignmentNullProduction(final Pattern currentGoal, final Pattern nextGoal, final AssignmentStatement inst, final Buffer bufferToQuery, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      boolean _isConstantOrFunctionAssignment = AssignmentGenerator.isConstantOrFunctionAssignment(inst, context);
      if (_isConstantOrFunctionAssignment) {
        return null;
      }
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      String _name = context._goalStateProvider.currentFunction().getName();
      String _plus = (_name + "-null-assignment-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      production.setName(_plus_1);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      Pattern cgp = currentGoal;
      Optional<BufferPattern> match = AssignmentGenerator.matchAsBufferNullPattern(inst, context);
      boolean _isPresent = match.isPresent();
      if (_isPresent) {
        Buffer _buffer = match.get().getBuffer();
        boolean _tripleEquals = (_buffer == goalBuffer);
        if (_tripleEquals) {
          cgp = ProcGenUtilities.merge(cgp, match.get().getPattern(), context);
        }
      }
      final Match goalCondition = ProcGenUtilities.toMatch(cgp, context);
      goalCondition.setName(goalBuffer);
      production.getConditions().add(goalCondition);
      if ((bufferToQuery != null)) {
        final Query query = ModelFragmentFactory.eINSTANCE.createQuery();
        query.setName(bufferToQuery);
        final ConditionalSlot bSlot = ProcGenUtilities.conditionalSlot("buffer", Condition.EQUALS);
        Value _value = bSlot.getValue();
        _value.setName("full");
        query.getSlots().add(bSlot);
        final ConditionalSlot sSlot = ProcGenUtilities.conditionalSlot("state", Condition.EQUALS);
        Value _value_1 = sSlot.getValue();
        _value_1.setName("free");
        query.getSlots().add(sSlot);
        production.getConditions().add(query);
      }
      if (((inst.getReference().getBuffer() != null) && (inst.getReference().getBuffer() != goalBuffer))) {
        final Optional<ChunkType> chunkType = context._bufferStateManager.getContentType(inst.getReference().getBuffer().getName());
        final Match bindingCondition = ModelFragmentFactory.eINSTANCE.createMatch();
        bindingCondition.setName(inst.getReference().getBuffer());
        bindingCondition.setIsa(ModelFragmentFactory.eINSTANCE.createIsaBlock());
        boolean _isPresent_1 = chunkType.isPresent();
        if (_isPresent_1) {
          IsaBlock _isa = bindingCondition.getIsa();
          _isa.setType(chunkType.get());
        } else {
          IsaBlock _isa_1 = bindingCondition.getIsa();
          String _name_1 = inst.getReference().getBuffer().getName();
          String _plus_2 = ("=" + _name_1);
          _isa_1.setName(_plus_2);
        }
        production.getConditions().add(bindingCondition);
      }
      if ((match.isPresent() && (match.get().getBuffer() != goalBuffer))) {
        final Match bindingCondition_1 = ProcGenUtilities.toMatch(match.get().getPattern(), context);
        bindingCondition_1.setName(match.get().getBuffer());
        production.getConditions().add(bindingCondition_1);
      }
      Pattern ngp = nextGoal;
      Optional<BufferPattern> modify = AssignmentGenerator.modifyAsBufferNullPattern(inst, context);
      if ((modify.isPresent() && (modify.get().getBuffer() == goalBuffer))) {
        ngp = ProcGenUtilities.merge(ngp, modify.get().getPattern(), context);
      }
      final Modify goalAction = ProcGenUtilities.toModify(ngp, context);
      goalAction.setName(goalBuffer);
      production.getActions().add(goalAction);
      if ((modify.isPresent() && (modify.get().getBuffer() != goalBuffer))) {
        final Modify bindingCondition_2 = ProcGenUtilities.toModify(modify.get().getPattern(), context);
        bindingCondition_2.setName(modify.get().getBuffer());
        production.getActions().add(bindingCondition_2);
      }
      if (((match.isPresent() && (match.get().getBuffer() != goalBuffer)) && ((!modify.isPresent()) || (modify.get().getBuffer() != match.get().getBuffer())))) {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(match.get().getBuffer());
        production.getActions().add(touch);
      }
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  /**
   * return the match necessary to bind for this statement. or maybe the BufferPattern??
   */
  public static Optional<BufferPattern> matchAsBufferPattern(final AssignmentStatement statement, final ProceduralGenerationContext context) {
    Optional<BufferPattern> _xblockexpression = null;
    {
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      Buffer buffer = statement.getAssignment().getBuffer();
      if (((buffer == null) && (statement.getAssignment().getValue().getName() != null))) {
        buffer = context._resolver.resolveBuffer(statement.getAssignment().getValue().getName());
        if ((buffer == null)) {
          buffer = goalBuffer;
        }
      }
      if (((((statement.getAssignment().getValue().getString() != null) || (statement.getAssignment().getValue().getNumber() != null)) || 
        (statement.getAssignment().getValue().getBoolean() != null)) || 
        (statement.getAssignment().getFunctionCall() != null))) {
        return Optional.<BufferPattern>empty();
      }
      final Function function = context._goalStateProvider.currentFunction();
      boolean recognizedSlot = true;
      if ((buffer == goalBuffer)) {
        final Function1<VariableStatement, Boolean> _function = (VariableStatement slot) -> {
          String _name = slot.getAssignment().getReference().getName();
          String _name_1 = statement.getAssignment().getValue().getName();
          return Boolean.valueOf(Objects.equal(_name, _name_1));
        };
        VariableStatement _findFirst = IterableExtensions.<VariableStatement>findFirst(function.getSlots(), _function);
        boolean _tripleNotEquals = (_findFirst != null);
        recognizedSlot = _tripleNotEquals;
        if ((!recognizedSlot)) {
          final Function1<VariableStatement, Boolean> _function_1 = (VariableStatement slot) -> {
            String _name = slot.getAssignment().getReference().getName();
            String _name_1 = statement.getAssignment().getValue().getName();
            return Boolean.valueOf(Objects.equal(_name, _name_1));
          };
          VariableStatement _findFirst_1 = IterableExtensions.<VariableStatement>findFirst(function.getVariables(), _function_1);
          boolean _tripleNotEquals_1 = (_findFirst_1 != null);
          recognizedSlot = _tripleNotEquals_1;
        }
      }
      if ((((statement.getAssignment().getBuffer() == null) && Objects.equal(buffer, goalBuffer)) && (!recognizedSlot))) {
        return Optional.<BufferPattern>empty();
      }
      final BufferPattern buffPattern = ProgramFactory.eINSTANCE.createBufferPattern();
      final ChunkType chunkType = context._bufferStateManager.getContentType(buffer.getName()).get();
      buffPattern.setBuffer(buffer);
      buffPattern.setPattern(ProgramFactory.eINSTANCE.createPattern());
      Pattern _pattern = buffPattern.getPattern();
      _pattern.setType(chunkType.getName());
      if (((buffer == goalBuffer) || (statement.getAssignment().getBuffer() != null))) {
        ConditionalSlot slot = ProcGenUtilities.conditionalSlot(statement.getAssignment().getValue().getName(), Condition.EQUALS);
        Value _value = slot.getValue();
        String _name = buffer.getName();
        String _plus = ("=" + _name);
        String _plus_1 = (_plus + "-");
        String _name_1 = statement.getAssignment().getValue().getName();
        String _plus_2 = (_plus_1 + _name_1);
        _value.setName(_plus_2);
        buffPattern.getPattern().getSlots().add(slot);
      }
      _xblockexpression = Optional.<BufferPattern>of(buffPattern);
    }
    return _xblockexpression;
  }
  
  public static Optional<BufferPattern> matchAsBufferNullPattern(final AssignmentStatement statement, final ProceduralGenerationContext context) {
    Optional<BufferPattern> _xblockexpression = null;
    {
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      Buffer buffer = statement.getAssignment().getBuffer();
      if (((buffer == null) && (statement.getAssignment().getValue().getName() != null))) {
        buffer = context._resolver.resolveBuffer(statement.getAssignment().getValue().getName());
        if ((buffer == null)) {
          buffer = goalBuffer;
        }
      }
      if (((((statement.getAssignment().getValue().getString() != null) || (statement.getAssignment().getValue().getNumber() != null)) || 
        (statement.getAssignment().getValue().getBoolean() != null)) || 
        (statement.getAssignment().getFunctionCall() != null))) {
        return Optional.<BufferPattern>empty();
      }
      final Function function = context._goalStateProvider.currentFunction();
      boolean recognizedSlot = true;
      if ((buffer == goalBuffer)) {
        final Function1<VariableStatement, Boolean> _function = (VariableStatement slot) -> {
          String _name = slot.getAssignment().getReference().getName();
          String _name_1 = statement.getAssignment().getValue().getName();
          return Boolean.valueOf(Objects.equal(_name, _name_1));
        };
        VariableStatement _findFirst = IterableExtensions.<VariableStatement>findFirst(function.getSlots(), _function);
        boolean _tripleNotEquals = (_findFirst != null);
        recognizedSlot = _tripleNotEquals;
        if ((!recognizedSlot)) {
          final Function1<VariableStatement, Boolean> _function_1 = (VariableStatement slot) -> {
            String _name = slot.getAssignment().getReference().getName();
            String _name_1 = statement.getAssignment().getValue().getName();
            return Boolean.valueOf(Objects.equal(_name, _name_1));
          };
          VariableStatement _findFirst_1 = IterableExtensions.<VariableStatement>findFirst(function.getVariables(), _function_1);
          boolean _tripleNotEquals_1 = (_findFirst_1 != null);
          recognizedSlot = _tripleNotEquals_1;
        }
      }
      if ((((statement.getAssignment().getBuffer() == null) && Objects.equal(buffer, goalBuffer)) && (!recognizedSlot))) {
        return Optional.<BufferPattern>empty();
      }
      final BufferPattern buffPattern = ProgramFactory.eINSTANCE.createBufferPattern();
      final ChunkType chunkType = context._bufferStateManager.getContentType(buffer.getName()).get();
      buffPattern.setBuffer(buffer);
      buffPattern.setPattern(ProgramFactory.eINSTANCE.createPattern());
      Pattern _pattern = buffPattern.getPattern();
      _pattern.setType(chunkType.getName());
      if (((buffer == goalBuffer) || (statement.getAssignment().getBuffer() != null))) {
        ConditionalSlot slot = ProcGenUtilities.conditionalSlot(statement.getAssignment().getValue().getName(), Condition.EQUALS);
        Value _value = slot.getValue();
        _value.setName("null");
        buffPattern.getPattern().getSlots().add(slot);
      }
      _xblockexpression = Optional.<BufferPattern>of(buffPattern);
    }
    return _xblockexpression;
  }
  
  public static Optional<BufferPattern> modifyAsBufferPattern(final AssignmentStatement statement, final ProceduralGenerationContext context) {
    Optional<BufferPattern> _xblockexpression = null;
    {
      Buffer buffer = statement.getReference().getBuffer();
      if ((buffer == null)) {
        buffer = context._resolver.resolveBuffer("goal");
      }
      final BufferPattern bufferPattern = ProgramFactory.eINSTANCE.createBufferPattern();
      bufferPattern.setBuffer(buffer);
      bufferPattern.setPattern(ProgramFactory.eINSTANCE.createPattern());
      final ConditionalSlot slot = ProcGenUtilities.conditionalSlot(statement.getReference().getName(), Condition.EQUALS);
      if (((statement.getAssignment().getValue().getName() != null) && context._functionTable.isFunction(statement.getAssignment().getValue().getName()))) {
        return Optional.<BufferPattern>empty();
      }
      if (((("null".equals(statement.getAssignment().getValue().getName()) || (statement.getAssignment().getValue().getString() != null)) || 
        (statement.getAssignment().getValue().getNumber() != null)) || (statement.getAssignment().getValue().getBoolean() != null))) {
        slot.setValue(EcoreUtil.<Value>copy(statement.getAssignment().getValue()));
      } else {
        Buffer _buffer = statement.getAssignment().getBuffer();
        boolean _tripleNotEquals = (_buffer != null);
        if (_tripleNotEquals) {
          Value _value = slot.getValue();
          String _name = statement.getAssignment().getBuffer().getName();
          String _plus = ("=" + _name);
          String _plus_1 = (_plus + "-");
          String _name_1 = statement.getAssignment().getValue().getName();
          String _plus_2 = (_plus_1 + _name_1);
          _value.setName(_plus_2);
        } else {
          Buffer _resolveBuffer = context._resolver.resolveBuffer(statement.getAssignment().getValue().getName());
          boolean _tripleNotEquals_1 = (_resolveBuffer != null);
          if (_tripleNotEquals_1) {
            Value _value_1 = slot.getValue();
            String _name_2 = statement.getAssignment().getValue().getName();
            String _plus_3 = ("=" + _name_2);
            _value_1.setName(_plus_3);
          } else {
            ChunkDef _resolveChunk = context._resolver.resolveChunk(statement.getAssignment().getValue().getName());
            boolean _tripleNotEquals_2 = (_resolveChunk != null);
            if (_tripleNotEquals_2) {
              Value _value_2 = slot.getValue();
              _value_2.setName(statement.getAssignment().getValue().getName());
            } else {
              Value _value_3 = slot.getValue();
              String _name_3 = statement.getAssignment().getValue().getName();
              String _plus_4 = ("=goal-" + _name_3);
              _value_3.setName(_plus_4);
            }
          }
        }
      }
      bufferPattern.getPattern().getSlots().add(slot);
      _xblockexpression = Optional.<BufferPattern>of(bufferPattern);
    }
    return _xblockexpression;
  }
  
  public static boolean isConstantOrFunctionAssignment(final AssignmentStatement statement, final ProceduralGenerationContext context) {
    if (((((statement.getAssignment().getValue().getString() != null) || (statement.getAssignment().getValue().getNumber() != null)) || 
      (statement.getAssignment().getValue().getBoolean() != null)) || 
      (statement.getAssignment().getFunctionCall() != null))) {
      return true;
    }
    final Function function = context._goalStateProvider.currentFunction();
    final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
    Buffer buffer = statement.getAssignment().getBuffer();
    final boolean loneBuffer = ((buffer == null) && (statement.getAssignment().getValue().getName() != null));
    if (((buffer == null) && (statement.getAssignment().getValue().getName() != null))) {
      buffer = context._resolver.resolveBuffer(statement.getAssignment().getValue().getName());
      if ((buffer == null)) {
        buffer = goalBuffer;
      }
    }
    boolean recognizedSlot = true;
    if ((buffer == goalBuffer)) {
      final Function1<VariableStatement, Boolean> _function = (VariableStatement slot) -> {
        String _name = slot.getAssignment().getReference().getName();
        String _name_1 = statement.getAssignment().getValue().getName();
        return Boolean.valueOf(Objects.equal(_name, _name_1));
      };
      VariableStatement _findFirst = IterableExtensions.<VariableStatement>findFirst(function.getSlots(), _function);
      boolean _tripleNotEquals = (_findFirst != null);
      recognizedSlot = _tripleNotEquals;
      if ((!recognizedSlot)) {
        final Function1<VariableStatement, Boolean> _function_1 = (VariableStatement slot) -> {
          String _name = slot.getAssignment().getReference().getName();
          String _name_1 = statement.getAssignment().getValue().getName();
          return Boolean.valueOf(Objects.equal(_name, _name_1));
        };
        VariableStatement _findFirst_1 = IterableExtensions.<VariableStatement>findFirst(function.getVariables(), _function_1);
        boolean _tripleNotEquals_1 = (_findFirst_1 != null);
        recognizedSlot = _tripleNotEquals_1;
      }
    }
    if (((statement.getAssignment().getBuffer() == null) && ((Objects.equal(buffer, goalBuffer) && (!recognizedSlot)) || (loneBuffer && (!Objects.equal(buffer, goalBuffer)))))) {
      return true;
    }
    return false;
  }
  
  public static Optional<BufferPattern> modifyAsBufferNullPattern(final AssignmentStatement statement, final ProceduralGenerationContext context) {
    Optional<BufferPattern> _xblockexpression = null;
    {
      Buffer buffer = statement.getReference().getBuffer();
      if ((buffer == null)) {
        buffer = context._resolver.resolveBuffer("goal");
      }
      final BufferPattern bufferPattern = ProgramFactory.eINSTANCE.createBufferPattern();
      bufferPattern.setBuffer(buffer);
      bufferPattern.setPattern(ProgramFactory.eINSTANCE.createPattern());
      final ConditionalSlot slot = ProcGenUtilities.conditionalSlot(statement.getReference().getName(), Condition.EQUALS);
      if (((statement.getAssignment().getValue().getName() != null) && context._functionTable.isFunction(statement.getAssignment().getValue().getName()))) {
        return Optional.<BufferPattern>empty();
      }
      if (((("null".equals(statement.getAssignment().getValue().getName()) || (statement.getAssignment().getValue().getString() != null)) || 
        (statement.getAssignment().getValue().getNumber() != null)) || (statement.getAssignment().getValue().getBoolean() != null))) {
        slot.setValue(EcoreUtil.<Value>copy(statement.getAssignment().getValue()));
      } else {
        Value _value = slot.getValue();
        _value.setName("null");
      }
      bufferPattern.getPattern().getSlots().add(slot);
      _xblockexpression = Optional.<BufferPattern>of(bufferPattern);
    }
    return _xblockexpression;
  }
}
