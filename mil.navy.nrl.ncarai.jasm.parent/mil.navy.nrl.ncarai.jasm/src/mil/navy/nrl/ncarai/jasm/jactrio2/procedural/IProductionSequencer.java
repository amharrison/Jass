package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import java.util.Collection;

import mil.navy.nrl.ncarai.jasm.program.Instruction;

public interface IProductionSequencer {

	public void sequence(Collection<? extends Instruction> blockOfInstructions, ProceduralGenerationContext context);
}
