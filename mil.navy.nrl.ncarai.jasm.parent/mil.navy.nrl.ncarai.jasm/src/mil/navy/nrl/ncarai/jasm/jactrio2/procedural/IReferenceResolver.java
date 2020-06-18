package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
import org.jactr.io2.jactr.modelFragment.ChunkType;

import mil.navy.nrl.ncarai.jasm.program.Program;

public interface IReferenceResolver {

	public ChunkDef resolveChunk(String name);

	public ChunkType resolveChunkType(String name);

	public Buffer resolveBuffer(String name);
	
	public void setProgram(Program program);
	
	public void add(ChunkDef chunk);
	
	public void add(ChunkType chunkType);
}
