package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.RemoveStatement
import mil.navy.nrl.ncarai.jasm.program.ReturnStatement
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class RemoveGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof RemoveStatement) {
      context.consumer.accept(createRemoveProduction(currentGoal, nextGoal, inst, context))
    }
  }

  def createRemoveProduction(Pattern currentGoal, Pattern nextGoal, RemoveStatement inst,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-Line" + ProcGenUtilities.lineNumber(inst)
    val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
    goalCondition.name = context._resolver.resolveBuffer("goal")
    rtn.conditions.add(goalCondition)
    
    inst.buffers.forEach[buffer | 
      val match = ModelFragmentFactory.eINSTANCE.createMatch
      match.name = buffer
      match.isa = ModelFragmentFactory.eINSTANCE.createIsaBlock
      match.isa.name = "="+buffer.name
      rtn.conditions.add(match)
    ]

    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = goalCondition.name
    rtn.actions.add(modify)
    
    inst.buffers.forEach[buffer | 
      val remove = ModelFragmentFactory.eINSTANCE.createRemove
      remove.name = buffer
      rtn.actions.add(remove)
    ]

    rtn
  }

  def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-remove"
  }

  override handles(Instruction instr) {
    instr instanceof RemoveStatement
  }

}
