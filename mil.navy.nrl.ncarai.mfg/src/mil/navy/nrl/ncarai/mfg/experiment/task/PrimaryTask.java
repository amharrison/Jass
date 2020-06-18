package mil.navy.nrl.ncarai.mfg.experiment.task;

import java.awt.Toolkit;

import org.jactr.core.model.IModel;
import org.jactr.tools.experiment.IExperiment;
import org.jactr.tools.experiment.actions.common.EndTrialAction;
import org.jactr.tools.experiment.actions.common.LockAction;
import org.jactr.tools.experiment.actions.common.LogAction;
import org.jactr.tools.experiment.actions.common.UnlockAction;
import org.jactr.tools.experiment.actions.jactr.MarkerAction;
import org.jactr.tools.experiment.actions.sensor.XMLSensorAction;
import org.jactr.tools.experiment.dc.DataCollector;
import org.jactr.tools.experiment.misc.ExperimentUtilities;
import org.jactr.tools.experiment.trial.impl.Trial;
import org.jactr.tools.experiment.triggers.ITrigger;
import org.jactr.tools.experiment.triggers.ImmediateTrigger;
import org.jactr.tools.experiment.triggers.TimeTrigger;

import mil.navy.nrl.ncarai.mfg.experiment.data.DataLog;

public class PrimaryTask extends Trial {

	int _block;

	int _responseCount;
	double _lastResponseTime;
	String _modelName;
	private double _duration;

	public PrimaryTask(String id, IExperiment experiment, String modelName, double duration, int block) {
		super(id, experiment);
		_block = block;
		_duration = duration;
		_modelName = modelName;
		initialize();
	}

	protected void initialize() {
		ITrigger trigger = new ImmediateTrigger(getExperiment());
		trigger.add(new LogAction("Starting trial ${trial} at ${time}", getExperiment()));
		trigger.add(new MarkerAction(_modelName, "trial", getId(), true, getExperiment()));
		trigger.add(new XMLSensorAction("mil/navy/nrl/ncarai/mfg/experiment/sensorData.xml", true, getExperiment()));
		String lockName = getExperiment().getVariableResolver()
				.resolve(_modelName, getExperiment().getVariableContext()).toString();
		trigger.add(new UnlockAction(lockName, getExperiment()));
		setStartTrigger(trigger);

		trigger = new ImmediateTrigger(getExperiment());
		trigger.add(new LogAction("Stopping trial ${trial} at ${time}", getExperiment()));
		trigger.add(new XMLSensorAction("mil/navy/nrl/ncarai/mfg/experiment/clear-display.xml", true, getExperiment()));
		trigger.add(new MarkerAction(_modelName, "trial", getId(), false, getExperiment()));
		trigger.add(new LockAction(lockName, getExperiment()));
		setEndTrigger(trigger);

		/*
		 * timeout to end the trial
		 */
		trigger = new TimeTrigger(_duration, true, getExperiment());
		trigger.add(new EndTrialAction(getExperiment()));
		addTrigger(trigger);
	}

	protected void started() {
		super.started();
		_lastResponseTime = getStartTime();
	}

	protected void stopped() {
		super.stopped();
//		if (_block > 0) {
//			IModel model = ExperimentUtilities.getExperimentsModel(getExperiment());
//			((DataLog) DataCollector.get().getSubjectData(model, true).getData()).dump();
//		}
	}

	protected boolean _exitNextRound = false;

	public void respond(int keypress) {
		System.err.print(".");

		_responseCount++;
		double reactionTime = getExperiment().getClock().getTime() - _lastResponseTime;
		_lastResponseTime = getExperiment().getClock().getTime();
		IModel model = ExperimentUtilities.getExperimentsModel(getExperiment());

//		if (_exitNextRound) {
//			try {
//				Thread.sleep(7000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.exit(-1);
//		}
		
		if (reactionTime > 10) {
			System.err.println("slowTrial @ " + getExperiment().getClock().getTime());

			Toolkit.getDefaultToolkit().beep();

			_exitNextRound = true;
		}

		if (_block > 0) {
			// actual data
			((DataLog) DataCollector.get().getSubjectData(model, true).getData()).log(_block - 1, _responseCount - 1,
					reactionTime);
		} else {
			// training
			((DataLog) DataCollector.get().getSubjectData(model, true).getData()).log(reactionTime);
		}
	}
}
