package mil.navy.nrl.ncarai.mfg.experiment.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jactr.tools.itr.fit.FitStatistics;
import org.jactr.tools.itr.ortho.ISliceAnalysis;
import org.jactr.tools.itr.ortho.ISliceAnalyzer;

import mil.navy.nrl.ncarai.mfg.experiment.data.DataLog;
import mil.navy.nrl.ncarai.mfg.experiment.data.SubjectDataProvider;

public class SliceAnalyzer implements ISliceAnalyzer {

	public SliceAnalyzer() {
	}

	@Override
	public Object analyze(ISliceAnalysis sliceAnalysis) {
		saveDataFile(sliceAnalysis);
		computeBasicFits(sliceAnalysis);

		SubjectDataProvider._groupData.clear();
		return null;
	}

	private void saveDataFile(ISliceAnalysis sliceAnalysis) {

		try {
			File dir = new File(System.getProperty("user.dir"), sliceAnalysis.getWorkingDirectory());
			dir.mkdirs();
			File fp = new File(dir, "data.txt");
			PrintWriter fw = new PrintWriter(new FileWriter(fp));
			fw.println("Block\tPosition");
			for (int i = 0; i < DataLog.MAX_BLOCKS; i++) {
				fw.format("%d\t", i + 1);
				for (int j = 0; j < DataLog.MAX_POSITIONS; j++)
					fw.format("%.2f\t", SubjectDataProvider._groupData.mean(i, j));
				fw.println();
			}
			fw.close();
			sliceAnalysis.addDetail("GroupData", "data.txt");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void computeBasicFits(ISliceAnalysis sliceAnalysis) {
		ReferenceData ref = new ReferenceData();
		double[][] comparisonPoints = new double[DataLog.MAX_BLOCKS * DataLog.MAX_POSITIONS][];
		for (int block = 0; block < DataLog.MAX_BLOCKS; block++)
			for (int position = 0; position < DataLog.MAX_POSITIONS; position++) {
				int index = block * DataLog.MAX_POSITIONS + position;
				comparisonPoints[index] = new double[] { ref.data[block][position],
						SubjectDataProvider._groupData.mean(block, position) };
			}
		FitStatistics fit = new FitStatistics(comparisonPoints);
		sliceAnalysis.addFitStatistics("Grouped", fit.getRMSE(), fit.getRSquared(), fit.getN(), false);
	}

}
