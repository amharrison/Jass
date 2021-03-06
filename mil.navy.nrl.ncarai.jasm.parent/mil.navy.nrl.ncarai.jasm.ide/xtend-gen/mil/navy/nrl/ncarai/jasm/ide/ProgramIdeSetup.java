/**
 * generated by Xtext 2.19.0
 */
package mil.navy.nrl.ncarai.jasm.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import mil.navy.nrl.ncarai.jasm.ProgramRuntimeModule;
import mil.navy.nrl.ncarai.jasm.ProgramStandaloneSetup;
import mil.navy.nrl.ncarai.jasm.ide.ProgramIdeModule;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
@SuppressWarnings("all")
public class ProgramIdeSetup extends ProgramStandaloneSetup {
  @Override
  public Injector createInjector() {
    ProgramRuntimeModule _programRuntimeModule = new ProgramRuntimeModule();
    ProgramIdeModule _programIdeModule = new ProgramIdeModule();
    return Guice.createInjector(Modules2.mixin(_programRuntimeModule, _programIdeModule));
  }
}
