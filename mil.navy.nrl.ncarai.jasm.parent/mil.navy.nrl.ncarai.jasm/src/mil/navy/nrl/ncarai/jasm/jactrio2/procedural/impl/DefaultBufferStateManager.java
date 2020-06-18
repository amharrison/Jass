package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.jactr.io2.jactr.modelFragment.ChunkType;

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IBufferStateManager;

public class DefaultBufferStateManager implements IBufferStateManager {

	private Map<String, ChunkType> _bufferMap = new TreeMap<>();
	
	public DefaultBufferStateManager() {
	
	}

	@Override
	public void setContentType(String bufferName, ChunkType chunkType) {
		_bufferMap.put(bufferName, chunkType);
	}

	@Override
	public Optional<ChunkType> getContentType(String bufferName) {
		ChunkType ct = _bufferMap.get(bufferName);
		if(ct!=null)
			return Optional.of(ct);
		return Optional.empty();
	}

	@Override
	public Set<String> getBufferNames() {
		return Collections.unmodifiableSet(_bufferMap.keySet());
	}

	@Override
	public void clear() {
		_bufferMap.clear();
	}

}
