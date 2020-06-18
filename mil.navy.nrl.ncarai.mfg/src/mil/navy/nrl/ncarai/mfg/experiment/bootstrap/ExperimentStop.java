package mil.navy.nrl.ncarai.mfg.experiment.bootstrap;

import java.io.IOException;

import org.jactr.core.model.IModel;
import org.jactr.core.runtime.ACTRRuntime;
import org.jactr.tools.experiment.bootstrap.StopModelExperiments;
import org.jactr.tools.experiment.dc.DataCollector;

public class ExperimentStop extends StopModelExperiments {

	public ExperimentStop() {
		// TODO Auto-generated constructor stub
	}

	public void run() {
		for (IModel model : ACTRRuntime.getRuntime().getModels())
			try {
				DataCollector.get().getSubjectData(model, true).save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		super.run();
	}

}
