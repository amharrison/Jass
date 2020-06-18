package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst;

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.InsertionStatement;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;
import org.jactr.io2.jactr.modelFragment.Match;
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory;
import org.jactr.io2.jactr.modelFragment.Modify;
import org.jactr.io2.jactr.modelFragment.Parameter;
import org.jactr.io2.jactr.modelFragment.Production;
import org.jactr.io2.jactr.modelFragment.Value;

@SuppressWarnings("all")
public class InsertionGenerator implements IProductionGenerator {
  @Override
  public void generate(final Pattern currentGoal, final Pattern nextGoal, final Instruction inst, final ProceduralGenerationContext context) {
    if ((inst instanceof InsertionStatement)) {
      context._goalStateProvider.startBranch("", context, ((InsertionStatement)inst).getEntryState(), ((InsertionStatement)inst).getExitState());
      context._goalStateProvider.nextState();
      final Pattern entryState = context._goalStateProvider.currentGoalState();
      context.getConsumer().accept(this.createEntryProduction(currentGoal, entryState, ((InsertionStatement)inst), context));
      context._goalStateProvider.nextState();
      final Pattern exitState = context._goalStateProvider.currentGoalState();
      boolean _isShouldGenerate = ((InsertionStatement)inst).isShouldGenerate();
      if (_isShouldGenerate) {
        context.getConsumer().accept(this.createBridgeProduction(entryState, exitState, ((InsertionStatement)inst), context));
      }
      context.getConsumer().accept(this.createExitProduction(exitState, nextGoal, ((InsertionStatement)inst), context));
      context._goalStateProvider.endBranch();
    }
  }
  
  public Production createBridgeProduction(final Pattern entryGoal, final Pattern exitGoal, final InsertionStatement inst, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-bridge-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      final String baseName = (_plus + Integer.valueOf(_lineNumber));
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      production.setName(baseName);
      final Match goalCondition = ProcGenUtilities.toMatch(entryGoal, context);
      goalCondition.setName(context._resolver.resolveBuffer("goal"));
      production.getConditions().add(goalCondition);
      final Modify goalModify = ProcGenUtilities.toModify(exitGoal, context);
      goalModify.setName(goalCondition.getName());
      production.getActions().add(goalModify);
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
  
  public Production createExitProduction(final Pattern entryGoal, final Pattern exitGoal, final InsertionStatement inst, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-exit-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      final String baseName = (_plus + Integer.valueOf(_lineNumber));
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      production.setName(baseName);
      final Match goalCondition = ProcGenUtilities.toMatch(entryGoal, context);
      goalCondition.setName(context._resolver.resolveBuffer("goal"));
      production.getConditions().add(goalCondition);
      final Modify goalModify = ProcGenUtilities.toModify(exitGoal, context);
      goalModify.setName(goalCondition.getName());
      production.getActions().add(goalModify);
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  public Production createEntryProduction(final Pattern entryGoal, final Pattern exitGoal, final InsertionStatement inst, final ProceduralGenerationContext context) {
    Production _xblockexpression = null;
    {
      String _computeBaseName = this.computeBaseName(context);
      String _plus = (_computeBaseName + "-entry-Line");
      int _lineNumber = ProcGenUtilities.lineNumber(inst);
      final String baseName = (_plus + Integer.valueOf(_lineNumber));
      final Production production = ModelFragmentFactory.eINSTANCE.createProduction();
      production.setName(baseName);
      final Match goalCondition = ProcGenUtilities.toMatch(entryGoal, context);
      goalCondition.setName(context._resolver.resolveBuffer("goal"));
      production.getConditions().add(goalCondition);
      final Modify goalModify = ProcGenUtilities.toModify(exitGoal, context);
      goalModify.setName(goalCondition.getName());
      production.getActions().add(goalModify);
      _xblockexpression = production;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean handles(final Instruction inst) {
    return (inst instanceof InsertionStatement);
  }
  
  public String computeBaseName(final ProceduralGenerationContext context) {
    String _xblockexpression = null;
    {
      final String functionName = context._goalStateProvider.currentFunction().getName();
      _xblockexpression = (functionName + "-insertion");
    }
    return _xblockexpression;
  }
}
