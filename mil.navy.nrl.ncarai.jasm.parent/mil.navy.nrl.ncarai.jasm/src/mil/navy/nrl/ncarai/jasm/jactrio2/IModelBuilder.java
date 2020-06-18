package mil.navy.nrl.ncarai.jasm.jactrio2;

import java.util.Collection;

import org.jactr.io2.jactr.modelFragment.ModelFragment;

import mil.navy.nrl.ncarai.jasm.program.Program;

public interface IModelBuilder {

	/**
	 * Build model(s) from the program. The first returned model should
	 * be the loader, that may or may not be edited by the modeler. The other models
	 * will be strictly generated, overwriting any changes.
	 * @param program
	 * @return
	 */
	public Collection<ModelFragment> build(Program program);
}
