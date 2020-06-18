package mil.navy.nrl.ncarai.mfg.episodic;

import java.util.Collection;
import java.util.Collections;

import org.jactr.core.buffer.IActivationBuffer;
import org.jactr.core.buffer.delegate.IDelegatedRequestableBuffer;
import org.jactr.core.module.AbstractModule;

public class EpisodicModule extends AbstractModule {

	private IDelegatedRequestableBuffer _episodicBuffer;

	public EpisodicModule() {
		super("episodic");
	}

	protected Collection<IActivationBuffer> createBuffers() {
		return Collections.singleton((IActivationBuffer) (_episodicBuffer = new EpisodicBuffer(this)));
	}

	

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

}
