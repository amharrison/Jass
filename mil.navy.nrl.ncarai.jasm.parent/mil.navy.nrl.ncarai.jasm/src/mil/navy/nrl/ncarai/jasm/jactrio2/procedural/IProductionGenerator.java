package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;

public interface IProductionGenerator {

	/**
	 * generate the necessary productions to implement Instruction inst, given the entry goal
	 * state and exit goal state.
	 * 
	 * @param currentGoal the current goal state
	 * @param nextGoal the goal state to set when complete
	 * @param inst
	 * @return
	 */
	public void generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context);
	
	/**
	 * does this generator handle this instruction
	 * @param instr
	 * @return
	 */
	public boolean handles(Instruction inst);
}
