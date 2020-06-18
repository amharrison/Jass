package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProcGenUtilities
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.AssignmentStatement
import mil.navy.nrl.ncarai.jasm.program.BufferPattern
import mil.navy.nrl.ncarai.jasm.program.Function
import mil.navy.nrl.ncarai.jasm.program.FunctionCallStatement
import mil.navy.nrl.ncarai.jasm.program.Instruction
import mil.navy.nrl.ncarai.jasm.program.Pattern
import org.eclipse.emf.ecore.util.EcoreUtil
import org.jactr.io2.jactr.modelFragment.Condition
import org.jactr.io2.jactr.modelFragment.ModelFragmentFactory

class FunctionalCallGenerator implements IProductionGenerator {

  override generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {

    if (inst instanceof FunctionCallStatement) {

      context._goalStateProvider.startBranch(currentGoal.slots.last.value.name, context, "executing")
      val executingState = context._goalStateProvider.nextGoalState

      context.consumer.accept(createInitialProduction(currentGoal, executingState, inst, inst.function, context))

      context._goalStateProvider.nextState

      if (inst.function.isChunk)
        context.consumer.accept(
          createTerminalProduction(executingState, nextGoal, inst, inst.function, context, "=return"))
      else if (inst.function.isVoid)
        context.consumer.accept(
          createTerminalProduction(executingState, nextGoal, inst, inst.function, context, "null"))

      context._goalStateProvider.endBranch
    }
  }

  static def createTerminalProduction(Pattern currentGoal, Pattern nextGoal, Instruction inst, Function function,
    ProceduralGenerationContext context, String returnValue) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    val nameStub = returnValue.replace("=", "-")
    rtn.name = computeBaseName(context) + "-" + function.name + "-return-" + nameStub + "-Line" +
      ProcGenUtilities.lineNumber(inst)
    val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
    val goalBuffer = context._resolver.resolveBuffer("goal")
    goalCondition.name = goalBuffer
    var slot = ProcGenUtilities.conditionalSlot("return", Condition.EQUALS)
    slot.value.name = returnValue
    goalCondition.slots.add(slot)
    rtn.conditions.add(goalCondition)

    var assignmentPattern = null as BufferPattern
    if(inst instanceof AssignmentStatement) 
    {
      assignmentPattern = ProcGenUtilities.asPattern(inst.reference, returnValue, context)
    }
    
    var ngp = nextGoal
    if(assignmentPattern!==null && assignmentPattern.buffer===goalBuffer)
     ngp = ProcGenUtilities.merge(ngp, assignmentPattern.pattern, context)
    val modify = ProcGenUtilities.toModify(ngp, context)
    modify.name = goalBuffer
    rtn.actions.add(modify)
    
    if(assignmentPattern!==null && assignmentPattern.buffer!==goalBuffer)
    {
      val assignment = ProcGenUtilities.toModify(assignmentPattern.pattern, context)
      assignment.name = assignmentPattern.buffer
      rtn.actions.add(assignment)
    }

    rtn
  }

  static def createInitialProduction(Pattern currentGoal, Pattern nextGoal, FunctionCallStatement inst,
    Function function, ProceduralGenerationContext context) {
    val rtn = ModelFragmentFactory.eINSTANCE.createProduction
    rtn.name = computeBaseName(context) + "-" + function.name + "-dispatch-Line" + ProcGenUtilities.lineNumber(inst)
//    val goalCondition = ProcGenUtilities.toMatch(currentGoal, context)
//    goalCondition.name = context._resolver.resolveBuffer("goal")
//    var slot = ProcGenUtilities.conditionalSlot("parameter", Condition.EQUALS)
//    slot.value.name = "=parameter"
//    goalCondition.slots.add(slot)
//    rtn.conditions.add(goalCondition)
//
//    context._goalManipulator.newGoal.ifPresent [ match |
//      rtn.conditions.add(match)
//    ]
    val goalBuffer = context._resolver.resolveBuffer("goal")
    val bufferConds = ProcGenUtilities.mergedPatternConditions(currentGoal, inst.parameters, context)
    var cond = ProcGenUtilities.toMatch(bufferConds.remove(goalBuffer), context)
    cond.name = goalBuffer
    rtn.conditions.add(cond)

    // now the rest
    bufferConds.keySet.forEach [ buffer |
      var condition = ProcGenUtilities.toMatch(bufferConds.get(buffer), context)
      condition.name = buffer
      rtn.conditions.add(condition)
    ]

//
    val modify = ProcGenUtilities.toModify(nextGoal, context)
    modify.name = goalBuffer
    rtn.actions.add(modify)

    val patternToAdd = ProcGenUtilities.patternActions(inst.parameters, context)

// null is possible
    val chunkType = context._resolver.resolveChunkType(function.name)
    if(chunkType==null) System.err.println("null chunkType for "+function.name);
    
    val add = context._goalManipulator.newGoal(chunkType, context)
    patternToAdd.slots.forEach [ slot |
      add.slots.add(EcoreUtil.copy(slot))
    ]

//    slot = ProcGenUtilities.conditionalSlot("parameter", Condition.EQUALS)
//    slot.value.name = "=parameter"
//    add.slots.add(slot)
    rtn.actions.add(add)

    /*
     * the touches
     */
    bufferConds.keySet.forEach [ buffer |
      val touch = ModelFragmentFactory.eINSTANCE.createModify
      touch.name = buffer
      rtn.actions.add(touch)
    ]
    
    

    rtn
  }

  static def computeBaseName(ProceduralGenerationContext context) {
    val functionName = context._goalStateProvider.currentFunction.name
    functionName + "-dispatch"
  }

  override handles(Instruction instr) {
    return instr instanceof FunctionCallStatement
  }

}
