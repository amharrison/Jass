package mil.navy.nrl.ncarai.jasm.jactrio2.procedural;

import java.util.function.Consumer;

import org.jactr.io2.jactr.modelFragment.Element;
import org.jactr.io2.jactr.modelFragment.Parameter;

import com.google.inject.Injector;

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.DefaultBlockSequencer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.DefaultBufferStateManager;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.DefaultFunctionTable;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.DefaultGoalManipulator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.DefaultGoalStateProvider;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.DefaultProductionPrioritizer;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.InstructionDispatcher;
import mil.navy.nrl.ncarai.jasm.program.Function;
import mil.navy.nrl.ncarai.jasm.program.GeneratorDef;

public class ProceduralGenerationContext {

	public final IReferenceResolver _resolver;
	public IGoalManipulator _goalManipulator;
	public IGoalStateProvider _goalStateProvider;
	public final InstructionDispatcher _instructionDispatcher;
	public final IProductionSequencer _productionSequencer;
	public final Consumer<Element> _consumer;

	public final IBufferStateManager _bufferStateManager;
	public final IFunctionTable _functionTable;
	public final IProductionPrioritizer _prioritizer = new DefaultProductionPrioritizer();

	public ProceduralGenerationContext(Injector injector, IReferenceResolver resolver, GeneratorDef generatorDef,
			Consumer<Element> consumer) {
		_resolver = resolver;

		try {
			String goalManipClass = getOrDefault(generatorDef, "IGoalManipulator",
					DefaultGoalManipulator.class.getName());
			_goalManipulator = (IGoalManipulator) injector
					.getInstance(getClass().getClassLoader().loadClass(goalManipClass));
		} catch (ClassNotFoundException e) {

			_goalManipulator = injector.getInstance(DefaultGoalManipulator.class);
		}

		_instructionDispatcher = injector.getInstance(InstructionDispatcher.class);
		_instructionDispatcher.installDefaults();
		
		_productionSequencer = new DefaultBlockSequencer();

		_bufferStateManager = new DefaultBufferStateManager();
		_functionTable = new DefaultFunctionTable();
		_consumer = consumer;
	}

	private String getOrDefault(GeneratorDef generator, String key, String defaultValue) {
		if (generator == null)
			return defaultValue;
		for (Parameter parameter : generator.getParameters().getParameter()) {
			if (parameter.getName().equals(key))
				return parameter.getValue().getString();
		}
		return defaultValue;
	}

	public Consumer<Element> getConsumer() {
		return _consumer;
	}

	public void newFunction(Function function) {

		_bufferStateManager.clear();

		_bufferStateManager.setContentType("goal", _resolver.resolveChunkType(function.getName()));

		/*
		 * and the assumed contents
		 */
		if (function.getAssumedContents() != null) {
			function.getAssumedContents().getAssumed().forEach(assumed -> {
				_bufferStateManager.setContentType(assumed.getBuffer().getName(),
						_resolver.resolveChunkType(assumed.getType()));
			});
		}

		_goalStateProvider = new DefaultGoalStateProvider(function, this);
	}

}
