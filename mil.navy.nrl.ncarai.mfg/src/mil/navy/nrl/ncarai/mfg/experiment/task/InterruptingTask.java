package mil.navy.nrl.ncarai.mfg.experiment.task;

import org.jactr.tools.experiment.IExperiment;
import org.jactr.tools.experiment.actions.common.EndTrialAction;
import org.jactr.tools.experiment.actions.common.LockAction;
import org.jactr.tools.experiment.actions.common.LogAction;
import org.jactr.tools.experiment.actions.common.UnlockAction;
import org.jactr.tools.experiment.actions.jactr.ClearAction;
import org.jactr.tools.experiment.actions.jactr.MarkerAction;
import org.jactr.tools.experiment.actions.sensor.XMLSensorAction;
import org.jactr.tools.experiment.trial.impl.Trial;
import org.jactr.tools.experiment.triggers.ITrigger;
import org.jactr.tools.experiment.triggers.ImmediateTrigger;
import org.jactr.tools.experiment.triggers.TimeTrigger;

public class InterruptingTask extends Trial {

	private double _duration;
	private String _modelName;

	public InterruptingTask(String id, IExperiment experiment, String modelName, double duration) {
		super(id, experiment);
		_duration = duration;
		_modelName = modelName;
		initialize();
	}

	protected void initialize() {
		ITrigger trigger = new ImmediateTrigger(getExperiment());
		trigger.add(new LogAction("Starting interrupt trial ${trial} at ${time}", getExperiment()));
		trigger.add(new MarkerAction(_modelName, "interrupt", getId(), true, getExperiment()));
		trigger.add(new ClearAction(_modelName, "goal,imaginal,episodic,retrieval,visual,visual-location", null,
				getExperiment()));
		trigger.add(new XMLSensorAction("mil/navy/nrl/ncarai/mfg/experiment/sensorData.xml", true, getExperiment()));

//		String lockName = getExperiment().getVariableResolver()
//				.resolve(_modelName, getExperiment().getVariableContext()).toString();
//		trigger.add(new UnlockAction(lockName, getExperiment()));
		setStartTrigger(trigger);

		trigger = new ImmediateTrigger(getExperiment());
		trigger.add(new LogAction("Stopping interrupt trial ${trial} at ${time}", getExperiment()));
		trigger.add(new XMLSensorAction("mil/navy/nrl/ncarai/mfg/experiment/clear-display.xml", true, getExperiment()));
		trigger.add(new MarkerAction(_modelName, "interrupt", getId(), false, getExperiment()));
//		trigger.add(new LockAction(lockName, getExperiment()));
		setEndTrigger(trigger);

		/*
		 * timeout to end the trial
		 */
		trigger = new TimeTrigger(_duration, true, getExperiment());
		trigger.add(new EndTrialAction(getExperiment()));
		addTrigger(trigger);

		/**
		 * time delay to screen clear and done display
		 */
		trigger = new TimeTrigger(_duration - 2, true, getExperiment());
		trigger.add(new XMLSensorAction("mil/navy/nrl/ncarai/mfg/experiment/clear-display.xml", true, getExperiment()));
		trigger.add(new XMLSensorAction("mil/navy/nrl/ncarai/mfg/experiment/done.xml", false, getExperiment()));
		addTrigger(trigger);
	}

}
