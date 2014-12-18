package job;

import java.util.List;

import job.manager.JobManager;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import file.FileBean;
import file.FileManager;
import file.filerecords.FileRecord;

public class ListJob extends Job {

	public ListJob() {
	}

	@Override
	public Job execute(FileManager filemanager) {
		List<FileBean> files = filemanager.getAllFiles();
		Job listResultJobForSending = new ListResultJob(files);
		return listResultJobForSending;	
	}

	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.LIST);
		return json.toString();
	}
}
