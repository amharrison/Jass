package mil.navy.nrl.ncarai.mfg.episodic;

import org.jactr.core.buffer.IActivationBuffer;
import org.jactr.core.buffer.delegate.AddChunkTypeRequestDelegate;
import org.jactr.core.buffer.delegate.DefaultDelegatedRequestableBuffer6;
import org.jactr.core.chunk.IChunk;
import org.jactr.core.module.IModule;
import org.jactr.core.runtime.ACTRRuntime;
import org.jactr.core.utils.ModelerException;
import org.jactr.fluent.FluentChunk;

public class EpisodicBuffer extends DefaultDelegatedRequestableBuffer6 implements IActivationBuffer {
	
	/*
	 * 
	 */
	static public final int NUMBER_OF_REFERENCES_TO_BUMP = 3;

	public EpisodicBuffer(IModule module) {
		super("episodic", module);

	}

	@Override
	public void initialize() {
		super.initialize();
		try {
			addRequestDelegate(
					new AddChunkTypeRequestDelegate(getModel().getDeclarativeModule().getChunkType("episode").get()));
		} catch (Exception e) {
			throw new ModelerException("Could not find episode chunktype", e);
		}
	}

	/*
	 * set the time of insertion
	 */
	protected void setSourceChunkInternal(IChunk sourceChunk) {
		if (sourceChunk != null) {
			FluentChunk.from(sourceChunk).slot("time", ACTRRuntime.getRuntime().getClock(getModel()).getTime()).build();
		} else {
			IChunk actualSource = getSourceChunk();
			if (actualSource != null) {
				double now = ACTRRuntime.getRuntime().getClock(getModel()).getTime();
				for (int i = 0; i < NUMBER_OF_REFERENCES_TO_BUMP; i++)
					actualSource.getSubsymbolicChunk().accessed(now);
			}
		}
		super.setSourceChunkInternal(sourceChunk);
	}
}
