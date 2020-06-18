package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl;

import java.util.Collection;
import java.util.function.Consumer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionSequencer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;

@SuppressWarnings("all")
public class DefaultBlockSequencer implements IProductionSequencer {
  @Override
  public void sequence(final Collection<? extends Instruction> blockOfInstructions, final ProceduralGenerationContext context) {
    final Consumer<Instruction> _function = (Instruction instr) -> {
      context._goalStateProvider.nextState();
      final Pattern current = context._goalStateProvider.currentGoalState();
      final Pattern next = context._goalStateProvider.nextGoalState();
      context._instructionDispatcher.generate(current, next, instr, context);
    };
    blockOfInstructions.forEach(_function);
  }
}
