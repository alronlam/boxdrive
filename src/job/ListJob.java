package job;

import java.util.List;

import org.vertx.java.core.json.JsonObject;

import server_manager.FileDirectory;
import serverjobs.CoordinatorJobManager;
import client.filerecords.FileRecord;

import commons.Constants;

import conn.Connection;

public class ListJob extends Job {

	ListJob(JsonObject json, Connection connection) {
		super(connection);
	}

	@Override
	public String executeLocal(JobManager jobManager) {

		// Assured here that this will be CoordinatorJobManager

		CoordinatorJobManager coordinatorJobManager = (CoordinatorJobManager) jobManager;
		FileDirectory fileDirectory = coordinatorJobManager.getFileDirectory();

		List<FileRecord> listOfFileRecords = fileDirectory.getListOfFileRecords();

		Job listResultJobForSending = new ListResultJob(listOfFileRecords, this.getConnection());
		jobManager.handleNewJob(listResultJobForSending);

		return null;
	}

	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.LIST);
		return json.toString();
	}

}
