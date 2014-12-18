package job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import file.FileBean;
import file.FileManager;
import file.filerecords.SyncManager;

public class ListResultJob extends Job {

	private List<FileBean> fileRecords;

	ListResultJob(List<FileBean> fileRecords) {
		this.fileRecords = fileRecords;
	}

	ListResultJob(JsonObject json) {
		this.fileRecords = new ArrayList<FileBean>();

		JsonArray folderRecordsJsonArray = json.getArray(Constants.JSON.BODY);
		
		@SuppressWarnings("rawtypes")
		Iterator iterator = folderRecordsJsonArray.iterator();

		while (iterator.hasNext()) {
			JsonObject fileRecord = (JsonObject) iterator.next();
			FileBean file = new FileBean(fileRecord);
			this.fileRecords.add(file);
		}
	}

	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.LIST_RESULT);

		JsonArray folderRecordsArray = new JsonArray();

		for (FileBean fileRecord : fileRecords) {

			JsonObject fileRecordJson = fileRecord.getJsonObject();
			folderRecordsArray.add(fileRecordJson);
		}

		json.putArray(Constants.JSON.BODY, folderRecordsArray);

		return json.toString();
	}

	@Override
	public Job execute(FileManager filemanager) {
		SyncManager.getInstance().initFileRecords(this.fileRecords);

		return null;
	}
}
