package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IFunctionTable;
import mil.navy.nrl.ncarai.jasm.program.Function;

public class DefaultFunctionTable implements IFunctionTable {

	private Map<String, Function> _functionTable = new TreeMap<>();

	public DefaultFunctionTable() {
	}

	@Override
	public Function getFunction(String name) {
		return _functionTable.get(name);
	}

	@Override
	public boolean isFunction(String name) {
		return _functionTable.containsKey(name);
	}

	public void populate(Collection<? extends Function> functions)
	{
		for (Function f : functions)
			_functionTable.put(f.getName(), f);
		
	}
	
}
