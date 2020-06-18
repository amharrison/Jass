package mil.navy.nrl.ncarai.mfg.experiment.data;

import org.jactr.tools.experiment.dc.DataCollector;

public class DataCollectorInit implements Runnable {

	@Override
	public void run() {
		
		DataCollector dc = new DataCollector(new SubjectDataProvider());
		DataCollector.set(dc);

	}

}
