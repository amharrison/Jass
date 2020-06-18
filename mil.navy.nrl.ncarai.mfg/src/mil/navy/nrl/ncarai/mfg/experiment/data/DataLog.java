package mil.navy.nrl.ncarai.mfg.experiment.data;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

public class DataLog {

	/*
	 * record of three blocks of response times for ten serial positions. We only
	 * log rt for the primary task
	 */
	final static public int MAX_POSITIONS = 10;
	final static public int MAX_BLOCKS = 3;

	private DescriptiveStatistics _trainingData = new DescriptiveStatistics(10);
	private SummaryStatistics[][] _data = new SummaryStatistics[MAX_BLOCKS][MAX_POSITIONS];

	public DataLog() {
		for (int i = 0; i < _data.length; i++)
			for (int j = 0; j < _data[i].length; j++)
				_data[i][j] = new SummaryStatistics();
	}

	public void log(int block, int position, double reactionTime) {
		if (position < MAX_POSITIONS)
			_data[block][position].addValue(reactionTime);
	}

	public void log(double reactionTime) {
		_trainingData.addValue(reactionTime);
//		System.err.println(String.format("Training %.2fs  (%.2fs (%.2f))", reactionTime, _trainingData.getMean(),
//				_trainingData.getStandardDeviation()));
	}

	public double mean(int block, int position) {
		return _data[block][position].getMean();
	}

	public double stddev(int block, int position) {
		return _data[block][position].getStandardDeviation();
	}

	public void group(DataLog subjectData) {
		for (int i = 0; i < _data.length; i++)
			for (int j = 0; j < _data[i].length; j++) {
				double value = subjectData._data[i][j].getMean();
				if (!Double.isNaN(value))
					_data[i][j].addValue(value);
			}
		dump();
	}

	public void dump() {
		for (int i = 0; i < _data.length; i++) {
			for (int j = 0; j < _data[i].length; j++)
				System.err.print(
						String.format("%.2f (%.2f)\t", _data[i][j].getMean(), _data[i][j].getStandardDeviation()));
			System.err.println();
		}

	}

	public void clear() {
		for (int i = 0; i < _data.length; i++)
			for (int j = 0; j < _data[i].length; j++)
				_data[i][j].clear();
		_trainingData.clear();
	}
}
