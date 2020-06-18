package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.jactr.io2.jactr.modelFragment.Add;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.modelFragment.Match;

import mil.navy.nrl.ncarai.jasm.program.Function;

public interface IGoalManipulator {
 
	/**
	 * return the match necessary to be injected before the add. This permits
	 * additional binding of variables if necessary
	 * @return
	 */
	public Optional<Match> newGoal();
	public Add newGoal(ChunkType goalType, ProceduralGenerationContext context);
	
	/**
	 * return the goal supertype of the chunktype returned by {@link #newGoalType(Function, ProceduralGenerationContext)}
	 * @param inTree an EObject that we use for scoping
	 * @return
	 */
	public ChunkType getGoalType(EObject inTree);
	
	/**
	 * create a chunktype describing a new goal, but do not add it to context's consumer
	 * @param name
	 * @param context
	 * @return
	 */
	public ChunkType newGoalType(Function function, ProceduralGenerationContext context);
	
	public List<String> getImports();
}
