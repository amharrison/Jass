package mil.navy.nrl.ncarai.mfg.experiment.handler;

import org.jactr.tools.experiment.IExperiment;
import org.jactr.tools.experiment.parser.handlers.INodeHandler;
import org.jactr.tools.experiment.trial.ITrial;
import org.w3c.dom.Element;

import mil.navy.nrl.ncarai.mfg.experiment.task.InterruptingTask;

public class InterruptingHandler implements INodeHandler<ITrial> {

	public InterruptingHandler() {
	}

	@Override
	public String getTagName() {
		return "interrupt";
	}

	@Override
	public ITrial process(Element element, IExperiment experiment) {
		return new InterruptingTask(element.getAttribute("id"), experiment, element.getAttribute("model"),
				Double.parseDouble(element.getAttribute("duration")));
	}

	@Override
	public boolean shouldDecend() {
		return false;
	}

}
