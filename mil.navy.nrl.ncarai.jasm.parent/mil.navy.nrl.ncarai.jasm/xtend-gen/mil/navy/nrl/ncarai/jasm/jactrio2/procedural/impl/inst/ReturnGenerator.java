package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import mil.navy.nrl.ncarai.jasm.program.ReturnStatement;
import mil.navy.nrl.ncarai.jasm.program.ReturnVariableReference;
import mil.navy.nrl.ncarai.jasm.program.VariableStatement;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.IsaBlock;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.SimpleSlot;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class ReturnGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof ReturnStatement)) {
      final Function function = IterableExtensions.<Function>last(Iterables.<Function>filter(EcoreUtil2.getAllContainers(inst), Function.class));
      boolean _isChunk = function.isChunk();
      if (_isChunk) {
        context.getConsumer().accept(this.createChunkReturnProduction(currentGoal, nextGoal, ((ReturnStatement)inst), context));
      } else {
        context.getConsumer().accept(this.createVoidReturnProduction(currentGoal, nextGoal, ((ReturnStatement)inst), context));
      }
    }
  }
  
  public Production createChunkReturnProduction(final Pattern currentGoal, final Pattern nextGoal, final ReturnStatement inst, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus_1 = (_plus + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_1);
      Buffer touchBuffer = ((Buffer) null);
      final boolean isLiteralReference = ((inst.getReference().getBuffer() == null) && (!this.isGoalSlot(inst.getReference(), context)));
      if (isLiteralReference) {
        final Match goalCondition = ProcGenUtilities.toMatch(currentGoal, context);
        goalCondition.setName(context._resolver.resolveBuffer("goal"));
        rtn.getConditions().add(goalCondition);
        final Modify action = ModelFragmentFactory.eINSTANCE.createModify();
        action.setName(goalCondition.getName());
        SimpleSlot slot = ProcGenUtilities.simpleSlot("stage");
        Value _value = slot.getValue();
        _value.setName("completed");
        action.getSlots().add(slot);
        slot = ProcGenUtilities.simpleSlot("return");
        slot.setValue(EcoreUtil.<Value>copy(inst.getReference().getValue()));
        action.getSlots().add(slot);
        rtn.getActions().add(action);
      } else {
        ReturnVariableReference _reference = inst.getReference();
        String _name = inst.getReference().getValue().getName();
        String _plus_2 = ("=" + _name);
        final BufferPattern variablePattern = ProcGenUtilities.asPattern(_reference, _plus_2, context);
        final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
        Buffer _buffer = variablePattern.getBuffer();
        boolean _equals = Objects.equal(_buffer, goalBuffer);
        if (_equals) {
          final Match goalCondition_1 = ProcGenUtilities.toMatch(
            ProcGenUtilities.merge(variablePattern.getPattern(), currentGoal, context), context);
          goalCondition_1.setName(goalBuffer);
          rtn.getConditions().add(goalCondition_1);
        } else {
          touchBuffer = variablePattern.getBuffer();
          final Match goalCondition_2 = ProcGenUtilities.toMatch(currentGoal, context);
          goalCondition_2.setName(goalBuffer);
          rtn.getConditions().add(goalCondition_2);
          final Match match = ProcGenUtilities.toMatch(variablePattern, context);
          if (((match.getIsa().getType() == null) && (match.getIsa().getChunk() == null))) {
            IsaBlock _isa = match.getIsa();
            String _name_1 = inst.getReference().getValue().getName();
            String _plus_3 = ("=" + _name_1);
            _isa.setName(_plus_3);
          }
          rtn.getConditions().add(match);
        }
        final Modify action_1 = ModelFragmentFactory.eINSTANCE.createModify();
        action_1.setName(goalBuffer);
        SimpleSlot slot_1 = ProcGenUtilities.simpleSlot("stage");
        Value _value_1 = slot_1.getValue();
        _value_1.setName("completed");
        action_1.getSlots().add(slot_1);
        slot_1 = ProcGenUtilities.simpleSlot("return");
        Value _value_2 = slot_1.getValue();
        String _name_2 = inst.getReference().getValue().getName();
        String _plus_4 = ("=" + _name_2);
        _value_2.setName(_plus_4);
        action_1.getSlots().add(slot_1);
        rtn.getActions().add(action_1);
        if ((touchBuffer != null)) {
          final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
          touch.setName(touchBuffer);
          rtn.getActions().add(touch);
        }
      }
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public Production createVoidReturnProduction(final Pattern currentGoal, final Pattern nextGoal, final ReturnStatement inst, final ProceduralGenerationContext context) {
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
      final Modify action = ModelFragmentFactory.eINSTANCE.createModify();
      action.setName(goalCondition.getName());
      SimpleSlot slot = ProcGenUtilities.simpleSlot("stage");
      Value _value = slot.getValue();
      _value.setName("completed");
      action.getSlots().add(slot);
      slot = ProcGenUtilities.simpleSlot("return");
      Value _value_1 = slot.getValue();
      _value_1.setName("null");
      action.getSlots().add(slot);
      rtn.getActions().add(action);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public boolean isGoalSlot(final ReturnVariableReference reference, final ProceduralGenerationContext context) {
    Buffer _buffer = reference.getBuffer();
    boolean _tripleNotEquals = (_buffer != null);
    if (_tripleNotEquals) {
      String _name = reference.getBuffer().getName();
      return Objects.equal(_name, "goal");
    }
    String _name_1 = reference.getValue().getName();
    boolean _tripleEquals = (_name_1 == null);
    if (_tripleEquals) {
      return false;
    }
    final Function function = IterableExtensions.<Function>last(Iterables.<Function>filter(EcoreUtil2.getAllContainers(reference), Function.class));
    final Function1<VariableStatement, Boolean> _function = (VariableStatement ass) -> {
      String _name_2 = ass.getAssignment().getReference().getName();
      String _name_3 = reference.getValue().getName();
      return Boolean.valueOf(Objects.equal(_name_2, _name_3));
    };
    VariableStatement any = IterableExtensions.<VariableStatement>findFirst(function.getSlots(), _function);
    if ((any == null)) {
      final Function1<VariableStatement, Boolean> _function_1 = (VariableStatement v) -> {
        String _name_2 = v.getAssignment().getReference().getName();
        String _name_3 = reference.getValue().getName();
        return Boolean.valueOf(Objects.equal(_name_2, _name_3));
      };
      any = IterableExtensions.<VariableStatement>findFirst(function.getVariables(), _function_1);
    }
    return (any != null);
  }
  
  public String computeBaseName(final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-return");
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof ReturnStatement);
  }
}
