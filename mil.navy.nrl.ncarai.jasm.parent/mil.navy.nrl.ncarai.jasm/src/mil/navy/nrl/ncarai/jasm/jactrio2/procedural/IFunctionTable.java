package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import java.util.Collection;

import mil.navy.nrl.ncarai.jasm.program.Function;

public interface IFunctionTable {

	public Function getFunction(String name);
	
	public boolean isFunction(String name);
	
	public void populate(Collection<? extends Function> functions);
}
