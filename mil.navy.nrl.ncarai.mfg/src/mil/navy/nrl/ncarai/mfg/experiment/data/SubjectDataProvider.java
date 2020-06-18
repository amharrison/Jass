package mil.navy.nrl.ncarai.mfg.experiment.data;

import java.io.IOException;

import org.jactr.tools.experiment.dc.data.ISubjectDataProvider;
import org.jactr.tools.experiment.dc.data.SubjectData;

public class SubjectDataProvider implements ISubjectDataProvider<DataLog> {

	static public final DataLog _groupData = new DataLog();
	
	@Override
	public DataLog newData() {
		return new DataLog();
	}

	@Override
	public void newSubject(SubjectData<?> data) {
		
	}

	@Override
	public void saveData(SubjectData<DataLog> data) throws IOException {
		
		//ideally save the individual data
		_groupData.group(data.getData());
	}
	
	public DataLog getGroupData() {
		return _groupData;
	}

}
