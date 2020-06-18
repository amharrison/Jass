package mil.navy.nrl.ncarai.mfg.experiment.keyboard;

import org.commonreality.identifier.IIdentifier;
import org.commonreality.sensors.handlers.EfferentCommandHandler;
import org.commonreality.sensors.keyboard.DefaultActuator;
import org.commonreality.sensors.keyboard.PressCommand;
import org.commonreality.sensors.keyboard.ReleaseCommand;
import org.jactr.core.model.IModel;
import org.jactr.core.runtime.ACTRRuntime;
import org.jactr.tools.experiment.IExperiment;
import org.jactr.tools.experiment.misc.ExperimentUtilities;
import org.jactr.tools.experiment.trial.ITrial;

import mil.navy.nrl.ncarai.mfg.experiment.task.PrimaryTask;

public class SimulatedKeyboard extends DefaultActuator {

	protected void press(PressCommand command, EfferentCommandHandler handler) {
		int keyCode = getCode(command, handler);

//		System.err.println("press "+keyCode);
		/*
		 */
		IIdentifier id = command.getEfferentIdentifier().getAgent();
		for (IModel model : ACTRRuntime.getRuntime().getModels()) {

			IIdentifier modelId = ACTRRuntime.getRuntime().getConnector().getAgent(model).getIdentifier();
			if (!modelId.equals(id))
				continue;

			IExperiment experiment = ExperimentUtilities.getModelsExperiment(model);
			ITrial currentTrial = experiment.getTrial();

			if (currentTrial instanceof PrimaryTask)
				((PrimaryTask) currentTrial).respond(keyCode);
			//we ignore the keystrokes during interruption task
		}
	}

	protected void release(ReleaseCommand command, EfferentCommandHandler handler) {
		int keyCode = getCode(command, handler);

		
	}

}
