package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.InsertionStatement
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class InsertionGenerator implements IProductionGenerator {
  
  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if(inst instanceof InsertionStatement)
    {
      /*
       * insertion point always creates two productions: entry & exit, and optionally a bridge production
       * this assumes entryState and exitState are unique
       */
       context._goalStateProvider.startBranch("", context, inst.entryState, inst.exitState)
       
       context._goalStateProvider.nextState
       val entryState = context._goalStateProvider.currentGoalState
       
       context.consumer.accept(createEntryProduction(currentGoal, entryState, inst, context))
       
       
       context._goalStateProvider.nextState
       val exitState = context._goalStateProvider.currentGoalState
       
       if(inst.shouldGenerate)
        context.consumer.accept(createBridgeProduction(entryState, exitState, inst, context))
       
       context.consumer.accept(createExitProduction(exitState, nextGoal, inst, context))
       
       context._goalStateProvider.endBranch
    }
  }
  
  def createBridgeProduction(Pattern entryGoal, Pattern exitGoal, InsertionStatement inst, ProceduralGenerationContext context) {
     val baseName = computeBaseName(context)+"-bridge-Line"+ProcGenUtilities.lineNumber(inst)
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = baseName
    
    val goalCondition = ProcGenUtilities.toMatch(entryGoal, context)
    goalCondition.name = context._resolver.resolveBuffer("goal")
    production.conditions.add(goalCondition)
    
    val goalModify = ProcGenUtilities.toModify(exitGoal, context)
    goalModify.name = goalCondition.name
    production.actions.add(goalModify)
    
    production.parameters = ModelFragmentFactory.eINSTANCE.createParametersBlock
    var p = ModelFragmentFactory.eINSTANCE.createParameter
    p.name = context._prioritizer.priorityParameterName
    p.value = ModelFragmentFactory.eINSTANCE.createValue
    p.value.string = context._prioritizer.fallbackPriority
    production.parameters.parameter.add(p)
    
    production
  }
  
  def createExitProduction(Pattern entryGoal, Pattern exitGoal, InsertionStatement inst, ProceduralGenerationContext context) {
     val baseName = computeBaseName(context)+"-exit-Line"+ProcGenUtilities.lineNumber(inst)
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = baseName
    
    val goalCondition = ProcGenUtilities.toMatch(entryGoal, context)
    goalCondition.name = context._resolver.resolveBuffer("goal")
    production.conditions.add(goalCondition)
    
    val goalModify = ProcGenUtilities.toModify(exitGoal, context)
    goalModify.name = goalCondition.name
    production.actions.add(goalModify)
    
    production
  }
  
  def createEntryProduction(Pattern entryGoal, Pattern exitGoal, InsertionStatement inst, ProceduralGenerationContext context) {
    val baseName = computeBaseName(context)+"-entry-Line"+ProcGenUtilities.lineNumber(inst)
    val production = ModelFragmentFactory.eINSTANCE.createProduction
    production.name = baseName
    
    val goalCondition = ProcGenUtilities.toMatch(entryGoal, context)
    goalCondition.name = context._resolver.resolveBuffer("goal")
    production.conditions.add(goalCondition)
    
    val goalModify = ProcGenUtilities.toModify(exitGoal, context)
    goalModify.name = goalCondition.name
    production.actions.add(goalModify)
    
    production
  }
  
  override handles(Instruction inst) {
    inst instanceof InsertionStatement
  }
  
   def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-insertion"
  }
}