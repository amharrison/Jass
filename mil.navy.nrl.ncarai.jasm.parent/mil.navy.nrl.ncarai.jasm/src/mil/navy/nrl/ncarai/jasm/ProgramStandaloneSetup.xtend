/*
 * generated by Xtext 2.19.0
 */
package mil.navy.nrl.ncarai.jasm


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class ProgramStandaloneSetup extends ProgramStandaloneSetupGenerated {

	def static void doSetup() {
		new ProgramStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
