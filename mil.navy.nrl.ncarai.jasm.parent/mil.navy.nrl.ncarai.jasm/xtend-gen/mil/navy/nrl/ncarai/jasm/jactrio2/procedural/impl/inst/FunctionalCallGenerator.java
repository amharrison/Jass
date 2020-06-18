package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import com.google.common.base.Objects;
import java.util.Map;
import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement;
import mil.navy.nrl.ncarai.jasm.program.BufferPattern;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.FunctionCallStatement;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jactr.io2.jactr.modelFragment.Add;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Condition;
import org.jactr.io2.jactr.modelFragment.ConditionalSlot;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class FunctionalCallGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof FunctionCallStatement)) {
      context._goalStateProvider.startBranch(IterableExtensions.<ConditionalSlot>last(currentGoal.getSlots()).getValue().getName(), context, "executing");
      final Pattern executingState = context._goalStateProvider.nextGoalState();
      context.getConsumer().accept(FunctionalCallGenerator.createInitialProduction(currentGoal, executingState, ((FunctionCallStatement)inst), ((FunctionCallStatement)inst).getFunction(), context));
      context._goalStateProvider.nextState();
      boolean _isChunk = ((FunctionCallStatement)inst).getFunction().isChunk();
      if (_isChunk) {
        context.getConsumer().accept(
          FunctionalCallGenerator.createTerminalProduction(executingState, nextGoal, inst, ((FunctionCallStatement)inst).getFunction(), context, "=return"));
      } else {
        boolean _isVoid = ((FunctionCallStatement)inst).getFunction().isVoid();
        if (_isVoid) {
          context.getConsumer().accept(
            FunctionalCallGenerator.createTerminalProduction(executingState, nextGoal, inst, ((FunctionCallStatement)inst).getFunction(), context, "null"));
        }
      }
      context._goalStateProvider.endBranch();
    }
  }
  
  public static Production createTerminalProduction(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final Function function, final ProceduralGenerationContext context, final String returnValue) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      final String nameStub = returnValue.replace("=", "-");
      String _computeBaseName = FunctionalCallGenerator.computeBaseName(context);
      String _plus = (_computeBaseName + "-");
      String _name = function.getName();
      String _plus_1 = (_plus + _name);
      String _plus_2 = (_plus_1 + "-return-");
      String _plus_3 = (_plus_2 + nameStub);
      String _plus_4 = (_plus_3 + "-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus_5 = (_plus_4 + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_5);
      final Match goalCondition = ProcGenUtilities.toMatch(currentGoal, context);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      goalCondition.setName(goalBuffer);
      ConditionalSlot slot = ProcGenUtilities.conditionalSlot("return", Condition.EQUALS);
      Value _value = slot.getValue();
      _value.setName(returnValue);
      goalCondition.getSlots().add(slot);
      rtn.getConditions().add(goalCondition);
      BufferPattern assignmentPattern = ((BufferPattern) null);
      if ((inst instanceof AssignmentStatement)) {
        assignmentPattern = ProcGenUtilities.asPattern(((AssignmentStatement)inst).getReference(), returnValue, context);
      }
      Pattern ngp = nextGoal;
      if (((assignmentPattern != null) && (assignmentPattern.getBuffer() == goalBuffer))) {
        ngp = ProcGenUtilities.merge(ngp, assignmentPattern.getPattern(), context);
      }
      final Modify modify = ProcGenUtilities.toModify(ngp, context);
      modify.setName(goalBuffer);
      rtn.getActions().add(modify);
      if (((assignmentPattern != null) && (assignmentPattern.getBuffer() != goalBuffer))) {
        final Modify assignment = ProcGenUtilities.toModify(assignmentPattern.getPattern(), context);
        assignment.setName(assignmentPattern.getBuffer());
        rtn.getActions().add(assignment);
      }
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public static Production createInitialProduction(final Pattern currentGoal, final Pattern nextGoal, final FunctionCallStatement inst, final Function function, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      final Production rtn = ModelFragmentFactory.eINSTANCE.createProduction();
      String _computeBaseName = FunctionalCallGenerator.computeBaseName(context);
      String _plus = (_computeBaseName + "-");
      String _name = function.getName();
      String _plus_1 = (_plus + _name);
      String _plus_2 = (_plus_1 + "-dispatch-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      String _plus_3 = (_plus_2 + Integer.valueOf(_lineNumber));
      rtn.setName(_plus_3);
      final Buffer goalBuffer = context._resolver.resolveBuffer("goal");
      final Map<Buffer, Pattern> bufferConds = ProcGenUtilities.mergedPatternConditions(currentGoal, inst.getParameters(), context);
      Match cond = ProcGenUtilities.toMatch(bufferConds.remove(goalBuffer), context);
      cond.setName(goalBuffer);
      rtn.getConditions().add(cond);
      final Consumer<Buffer> _function = (Buffer buffer) -> {
        Match condition = ProcGenUtilities.toMatch(bufferConds.get(buffer), context);
        condition.setName(buffer);
        rtn.getConditions().add(condition);
      };
      bufferConds.keySet().forEach(_function);
      final Modify modify = ProcGenUtilities.toModify(nextGoal, context);
      modify.setName(goalBuffer);
      rtn.getActions().add(modify);
      final Pattern patternToAdd = ProcGenUtilities.patternActions(inst.getParameters(), context);
      final ChunkType chunkType = context._resolver.resolveChunkType(function.getName());
      boolean _equals = Objects.equal(chunkType, null);
      if (_equals) {
        String _name_1 = function.getName();
        String _plus_4 = ("null chunkType for " + _name_1);
        System.err.println(_plus_4);
      }
      final Add add = context._goalManipulator.newGoal(chunkType, context);
      final Consumer<ConditionalSlot> _function_1 = (ConditionalSlot slot) -> {
        add.getSlots().add(EcoreUtil.<ConditionalSlot>copy(slot));
      };
      patternToAdd.getSlots().forEach(_function_1);
      rtn.getActions().add(add);
      final Consumer<Buffer> _function_2 = (Buffer buffer) -> {
        final Modify touch = ModelFragmentFactory.eINSTANCE.createModify();
        touch.setName(buffer);
        rtn.getActions().add(touch);
      };
      bufferConds.keySet().forEach(_function_2);
      _xblockexpression = rtn;
    }
    return _xblockexpression;
  }
  
  public static String computeBaseName(final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-dispatch");
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction instr) {
    return (instr instanceof FunctionCallStatement);
  }
}
