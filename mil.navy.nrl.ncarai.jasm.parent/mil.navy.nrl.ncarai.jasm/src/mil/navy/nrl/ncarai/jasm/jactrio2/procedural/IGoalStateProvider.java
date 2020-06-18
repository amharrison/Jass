package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.Pattern;

public interface IGoalStateProvider {

	/**
	 * return the current function we are in
	 * 
	 * @return
	 */
	public Function currentFunction();

	/**
	 * the goal state before the current one
	 * 
	 * @return
	 */
	public Pattern priorGoalState();

	/**
	 * pattern describing the current goal state. The last slot of which should be
	 * the state slot
	 * 
	 * @return
	 */
	public Pattern currentGoalState();

	/**
	 * pattern describing the next intended goal state. The last slot of which
	 * should be the state slot
	 * 
	 * @return
	 */
	public Pattern nextGoalState();

	/**
	 * increment the state machine to generate a new {@link #nextGoalState()} and
	 * making the current next the {@link #currentGoalState()}. This should be
	 * called once for each segment of the current branch.
	 */
	public void nextState();

	/**
	 * branch off a new series of states named rootStateChunk_stage[i]. This is
	 * useful to split a current state and inject new ones
	 * 
	 * @param rootStateChunk
	 * @param context
	 * @param stages
	 */
	public void startBranch(String rootStateChunk, ProceduralGenerationContext context, String... stages);

	/**
	 * branch off a new series of stages, one for each line of the implicitBlock. No
	 * checking is done to ensure that each instruction is on a different line.
	 * 
	 * @param implicitBlock
	 * @param context
	 */
	public void startBranch(Collection<? extends EObject> implicitBlock, ProceduralGenerationContext context);

	public void startBranch(Collection<? extends EObject> implicitBlock, String rootStateChunkName,
			ProceduralGenerationContext context, String... stages);

	public void endBranch();
}
