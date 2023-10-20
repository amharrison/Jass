package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl

import com.google.inject.Inject
import java.util.Map
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IReferenceResolver
import mil.navy.nrl.ncarai.jasm.program.Program
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities
import org.eclipse.emf.ecore.EObject
import org.jactr.io2.jactr.modelFragment.Buffer
import org.jactr.io2.jactr.modelFragment.ChunkDef
import org.jactr.io2.jactr.modelFragment.ChunkType
import org.jactr.io2.jactr.validation.ModelFragmentUtil

class DefaultReferenceResolver implements IReferenceResolver {

  @Inject ModelFragmentUtil _utilities
  @Inject ValidationUtilities _validation
  Map<String, EObject> _chunks;
  Map<String, EObject> _types;
  Map<String, EObject> _buffers;

  override setProgram(Program program) {
    _chunks = _utilities.resolveSymbolTable(program.eResource, _validation.chunkTable(program))
    _types = _utilities.resolveSymbolTable(program.eResource, _validation.chunkTypeTable(program))
    _buffers = _utilities.resolveSymbolTableAny(program.eResource, _validation.bufferTable(program))
  }

  override resolveBuffer(String name) {
    _buffers.get(name) as Buffer
  }

  override resolveChunk(String name) {
    _chunks.get(name) as ChunkDef
  }

  override resolveChunkType(String name) {
    if(name===null) return null
    _types.get(name) as ChunkType
  }

  override add(ChunkDef chunk){
    _chunks.put(chunk.name, chunk)
  }
  
  override add(ChunkType chunkType){
    _types.put(chunkType.name, chunkType)
  }
}
