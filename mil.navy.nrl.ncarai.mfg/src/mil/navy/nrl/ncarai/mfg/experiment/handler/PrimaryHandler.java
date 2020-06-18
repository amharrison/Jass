package mil.navy.nrl.ncarai.mfg.experiment.handler;

import org.jactr.tools.experiment.IExperiment;
import org.jactr.tools.experiment.parser.handlers.INodeHandler;
import org.jactr.tools.experiment.trial.ITrial;
import org.w3c.dom.Element;

import mil.navy.nrl.ncarai.mfg.experiment.task.PrimaryTask;

public class PrimaryHandler implements INodeHandler<ITrial> {

	public PrimaryHandler() {
	}

	@Override
	public String getTagName() {
		return "primary";
	}

	@Override
	public ITrial process(Element element, IExperiment experiment) {
		return new PrimaryTask(element.getAttribute("id"), experiment, element.getAttribute("model"),
				Double.parseDouble(element.getAttribute("duration")), Integer.parseInt(element.getAttribute("block")));
	}

	@Override
	public boolean shouldDecend() {
		return false;
	}

}
