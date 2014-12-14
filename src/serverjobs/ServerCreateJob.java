package serverjobs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

import job.CreateJob;
import job.Job;
import job.JobManager;
import job.RequestJob;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import conn.Connection;

public class ServerCreateJob extends ServerBasicJob {

	public ServerCreateJob(Path path, Connection connection) {
		super(path, connection);
	}

	public ServerCreateJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	public String executeLocal(CoordinatorJobManager jobManager) {
		List<Connection> connections = jobManager.getFileDirectory().getServerListForFile(file);
		
		for (Connection c : connections) {
			Job forSending = new CreateJob(file.getJsonObject(), c);
			jobManager.handleNewJob(forSending);
		}
		
		return null;
	}
	
	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.CREATE);
		JsonObject body = file.getJsonObject();
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}

}