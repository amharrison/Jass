package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl;

import java.util.Map;
import javax.inject.Inject;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IReferenceResolver;
import mil.navy.nrl.ncarai.jasm.program.Program;
import mil.navy.nrl.ncarai.jasm.validation.ValidationUtilities;
import org.eclipse.emf.ecore.EObject;
import org.jactr.io2.jactr.modelFragment.Buffer;
import org.jactr.io2.jactr.modelFragment.ChunkDef;
import org.jactr.io2.jactr.modelFragment.ChunkType;
import org.jactr.io2.jactr.validation.ModelFragmentUtil;

@SuppressWarnings("all")
public class DefaultReferenceResolver implements IReferenceResolver {
  @Inject
  private ModelFragmentUtil _utilities;
  
  @Inject
  private ValidationUtilities _validation;
  
  private Map<String, EObject> _chunks;
  
  private Map<String, EObject> _types;
  
  private Map<String, EObject> _buffers;
  
  @Override
  public void setProgram(final Program program) {
    this._chunks = this._utilities.resolveSymbolTable(program.eResource(), this._validation.chunkTable(program));
    this._types = this._utilities.resolveSymbolTable(program.eResource(), this._validation.chunkTypeTable(program));
    this._buffers = this._utilities.resolveSymbolTableAny(program.eResource(), this._validation.bufferTable(program));
  }
  
  @Override
  public Buffer resolveBuffer(final String name) {
    EObject _get = this._buffers.get(name);
    return ((Buffer) _get);
  }
  
  @Override
  public ChunkDef resolveChunk(final String name) {
    EObject _get = this._chunks.get(name);
    return ((ChunkDef) _get);
  }
  
  @Override
  public ChunkType resolveChunkType(final String name) {
    ChunkType _xblockexpression = null;
    {
      if ((name == null)) {
        return null;
      }
      EObject _get = this._types.get(name);
      _xblockexpression = ((ChunkType) _get);
    }
    return _xblockexpression;
  }
  
  @Override
  public void add(final ChunkDef chunk) {
    this._chunks.put(chunk.getName(), chunk);
  }
  
  @Override
  public void add(final ChunkType chunkType) {
    this._types.put(chunkType.getName(), chunkType);
  }
}
