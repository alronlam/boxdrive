package job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import client.filerecords.FileRecord;
import client.filerecords.SyncManager;

import commons.Constants;

import conn.Connection;

public class ListResultJob extends Job {

	private List<FileRecord> fileRecords;

	ListResultJob(List<FileRecord> fileRecords, Connection connection) {
		super(connection);
		this.fileRecords = fileRecords;
	}

	ListResultJob(JsonObject json, Connection connection) {
		super(connection);

		this.fileRecords = new ArrayList<FileRecord>();

		JsonArray folderRecordsJsonArray = json.getArray(Constants.JSON.BODY);
		@SuppressWarnings("rawtypes")
		Iterator iterator = folderRecordsJsonArray.iterator();

		while (iterator.hasNext()) {
			JsonObject fileRecord = (JsonObject) iterator.next();

			String fileName = fileRecord.getString(Constants.Body.FILENAME);
			long lastModified = fileRecord.getLong(Constants.Body.LAST_MODIFIED);

			FileRecord newfileRecord = new FileRecord(fileName, lastModified);
			this.fileRecords.add(newfileRecord);
		}
	}

	@Override
	public String executeLocal(JobManager jobManager) {

		SyncManager.getInstance().initFileRecords(this.fileRecords);

		return null;
	}

	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.LIST_RESULT);

		JsonArray folderRecordsArray = new JsonArray();

		for (FileRecord fileRecord : fileRecords) {

			JsonObject fileRecordJson = new JsonObject();
			fileRecordJson.putString(Constants.Body.FILENAME, fileRecord.getFileName());
			fileRecordJson.putNumber(Constants.Body.LAST_MODIFIED, fileRecord.getDateTimeModified());

			folderRecordsArray.add(fileRecordJson);
		}

		json.putArray(Constants.JSON.BODY, folderRecordsArray);

		return json.toString();
	}
}
