package serverjobs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import job.CreateJob;
import job.DeleteJob;
import job.Job;
import job.JobManager;

import org.vertx.java.core.json.JsonObject;

import client.filerecords.ClientFileRecordManager;
import commons.Constants;
import conn.Connection;

public class ServerDeleteJob extends ServerBasicJob {

	public ServerDeleteJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	public ServerDeleteJob(Path path, long lastModified, Connection connection) {
		super(path, connection);
		file.setLastModified(lastModified);
	}

	@Override
	public String executeLocal(CoordinatorJobManager jobManager) {
		// broadcast to all servers that need to delete the file
		List<Connection> connections = jobManager.getFileDirectory().getServerListForFile(file);
		
		for (Connection c : connections) {
			Job forSending = new DeleteJob(file.getJsonObject(), c);
			jobManager.handleNewJob(forSending);
		}
		
		return null;
	}
	
	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.DELETE);
		JsonObject body = file.getJsonObject();
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}
}
