package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionSequencer
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext
import mil.navy.nrl.ncarai.jasm.program.Block
import mil.navy.nrl.ncarai.jasm.program.Instruction
import java.util.Collection

class DefaultBlockSequencer implements IProductionSequencer {

  
  override sequence(Collection<? extends Instruction> blockOfInstructions, ProceduralGenerationContext context) {

    
    blockOfInstructions.forEach [ instr |
      // increment our state
      context._goalStateProvider.nextState()
      val current = context._goalStateProvider.currentGoalState
      val next = context._goalStateProvider.nextGoalState

      
      context._instructionDispatcher.generate(current, next, instr, context)
    ]
    
  }

}
