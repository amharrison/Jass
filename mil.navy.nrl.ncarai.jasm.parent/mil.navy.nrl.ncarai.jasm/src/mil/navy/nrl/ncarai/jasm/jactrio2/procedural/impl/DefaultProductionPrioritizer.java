package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl;

import org.jactr.core.production.six.ISubsymbolicProduction6;

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionPrioritizer;

public class DefaultProductionPrioritizer implements IProductionPrioritizer {

	public DefaultProductionPrioritizer() {

	}

	@Override
	public String getPriorityParameterName() {
		return ISubsymbolicProduction6.UTILITY_PARAM;
	}

	@Override
	public String getFallbackPriority() {
		return "-1";
	}

	@Override
	public String getDefaultPriority() {
		return "0";
	}

}
