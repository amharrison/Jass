/*
 * generated by Xtext 2.20.0
 */
package mil.navy.nrl.ncarai.jasm.ui.tests;

import com.google.inject.Injector;
import mil.navy.nrl.ncarai.jasm.ui.internal.JasmActivator;
import org.eclipse.xtext.testing.IInjectorProvider;

public class ProgramUiInjectorProvider implements IInjectorProvider {

	@Override
	public Injector getInjector() {
		return JasmActivator.getInstance().getInjector("mil.navy.nrl.ncarai.jasm.Program");
	}

}
