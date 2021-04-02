package mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.inject.Injector;

import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.IProductionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.ProceduralGenerationContext;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.AssignmentGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.BufferRequestGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.DoWhileGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.ForEachGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.FunctionalCallGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.IfThenElseGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.InsertionGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.MatchCaseGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.OutputGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.RemoveGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.RepeatUntilGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.ReturnGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.RewardGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.UntilRepeatGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.WaitGenerator;
import mil.navy.nrl.ncarai.jasm.jactrio2.procedural.impl.inst.WhileDoGenerator;
import mil.navy.nrl.ncarai.jasm.program.Instruction;
import mil.navy.nrl.ncarai.jasm.program.Pattern;

public class InstructionDispatcher {

	@Inject Injector _injector;
	
	private List<IProductionGenerator> _productionGenerators = new ArrayList<>();

	public InstructionDispatcher() {

	}

	public void installDefaults() {
		Class[] generators = {AssignmentGenerator.class, IfThenElseGenerator.class,
				FunctionalCallGenerator.class, BufferRequestGenerator.class,
				ReturnGenerator.class, RemoveGenerator.class, ForEachGenerator.class,
				OutputGenerator.class, WhileDoGenerator.class, DoWhileGenerator.class,
				InsertionGenerator.class, RepeatUntilGenerator.class, UntilRepeatGenerator.class,
				MatchCaseGenerator.class, RewardGenerator.class, WaitGenerator.class};
		
		for(Class<IProductionGenerator> c : generators) {
			_productionGenerators.add(_injector.getInstance(c));
		}

	}

	public void generate(Pattern currentGoal, Pattern nextGoal, Instruction inst, ProceduralGenerationContext context) {
		IProductionGenerator generator = getGenerator(inst);
		if (generator != null)
			generator.generate(currentGoal, nextGoal, inst, context);
//		else
//			throw new UnsupportedOperationException(inst + " not implemented");
	}

	public IProductionGenerator getGenerator(Instruction inst) {
		for (IProductionGenerator gen : _productionGenerators)
			if (gen.handles(inst))
				return gen;
		return null;
	}
}
