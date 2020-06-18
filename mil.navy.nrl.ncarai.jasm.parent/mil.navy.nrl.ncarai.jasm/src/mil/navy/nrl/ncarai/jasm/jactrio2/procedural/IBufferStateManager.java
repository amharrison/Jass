package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import java.util.Optional;
import java.util.Set;

import org.jactr.io2.jactr.modelFragment.ChunkType;

public interface IBufferStateManager {

	public void setContentType(String bufferName, ChunkType chunkType);
	
	public Optional<ChunkType> getContentType(String bufferName);
	
	public Set<String> getBufferNames();
	
	public void clear();
}
