package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import mil.navy.nrl.ncarai.jasm.program.ReturnStatement
import mil.navy.nrl.ncarai.jasm.program.ReturnVariableReference
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.EcoreUtil2
import org.jactr.io2.jactr.modelFragment.Buffer
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class ReturnGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
    if (inst instanceof ReturnStatement) {
      val function = EcoreUtil2.getAllContainers(inst).filter(Function).last

      if (function.isChunk)
        context.consumer.accept(createChunkReturnProduction(currentGoal, nextGoal, inst, context))
      else
        context.consumer.accept(createVoidReturnProduction(currentGoal, nextGoal, inst, context))
    }
  }

  def createChunkReturnProduction(Pattern currentGoal, Pattern nextGoal, ReturnStatement inst,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-Line" + ProcGenUtilities.lineNumber(inst)

    var touchBuffer = null as Buffer
    val isLiteralReference = inst.reference.buffer === null && !isGoalSlot(inst.reference, context)

    if (isLiteralReference) {
      val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
      goalCondition.name = context._resolver.resolveBuffer("goal")
      rtn.conditions.add(goalCondition)

      val action = ModelFragmentFactory.eINSTANCE.createModify
      action.name = goalCondition.name
      var slot = ProcGenUtilities.simpleSlot("stage")
      slot.value.name = "completed"
      action.slots.add(slot)
      slot = ProcGenUtilities.simpleSlot("return")
      //handles all possible value types
      slot.value = EcoreUtil.copy(inst.reference.value)      
      action.slots.add(slot)
      rtn.actions.add(action)
    } else {
      val variablePattern = ProcGenUtilities.asPattern(inst.reference, "=" + inst.reference.value.name, context)
      val goalBuffer = context._resolver.resolveBuffer("goal")

      if (variablePattern.buffer == goalBuffer) {
        val goalCondition = ProcGenUtilities.toMatch(
          ProcGenUtilities.merge(variablePattern.pattern, currentGoal, context), context)
        goalCondition.name = goalBuffer
        rtn.conditions.add(goalCondition)
      } else {
        
        touchBuffer = variablePattern.buffer
        
        val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
        goalCondition.name = goalBuffer
        rtn.conditions.add(goalCondition)
        
        val match = ProcGenUtilities.toMatch(variablePattern, context)
        /*
         * if we couldn't infer the type or chunk, it's a variable reference
         */
        if(match.isa.type===null && match.isa.chunk===null)
         match.isa.name = "=" + inst.reference.value.name
        
        rtn.conditions.add(match)
      }

      val action = ModelFragmentFactory.eINSTANCE.createModify
      action.name = goalBuffer
      var slot = ProcGenUtilities.simpleSlot("stage")
      slot.value.name = "completed"
      action.slots.add(slot)
      slot = ProcGenUtilities.simpleSlot("return")
      slot.value.name = "=" + inst.reference.value.name
      action.slots.add(slot)
      rtn.actions.add(action)
      
      if(touchBuffer!==null)
      {
        val touch = ModelFragmentFactory.eINSTANCE.createModify
        touch.name = touchBuffer
        rtn.actions.add(touch)
      }
    }

    rtn
  }

  def createVoidReturnProduction(Pattern currentGoal, Pattern nextGoal, ReturnStatement inst,
    ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-Line" + ProcGenUtilities.lineNumber(inst)

    val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
    goalCondition.name = context._resolver.resolveBuffer("goal")

    rtn.conditions.add(goalCondition)

    val action = ModelFragmentFactory.eINSTANCE.createModify
    action.name = goalCondition.name
    var slot = ProcGenUtilities.simpleSlot("stage")
    slot.value.name = "completed"
    action.slots.add(slot)
    slot = ProcGenUtilities.simpleSlot("return")
    slot.value.name = "null"

    action.slots.add(slot)

    rtn.actions.add(action)

    rtn
  }

  def isGoalSlot(ReturnVariableReference reference, ProceduralGenerationContext context) {
    if (reference.buffer !== null)
      return reference.buffer.name == "goal"
    if(reference.value.name===null) return false
    val function = EcoreUtil2.getAllContainers(reference).filter(Function).last
    var any = function.slots.findFirst[ass|ass.assignment.reference.name == reference.value.name]
    if (any === null)
      any = function.variables.findFirst[v|v.assignment.reference.name == reference.value.name]
    return any !== null
  }

  def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-return"
  }

  override handles(Instruction instr) {
    instr instanceof ReturnStatement
  }

}
