package serverjobs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import job.BasicJob;
import job.FileJob;
import job.Job;
import job.JobManager;
import job.RequestJob;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import conn.Connection;

public class ServerRequestJob extends ServerBasicJob {

	/**
	 * Creates a Request with the same parameters as the job argument.
	 * 
	 * @param job
	 */
	public ServerRequestJob(ServerBasicJob job) {
		super(job);
	}

	public ServerRequestJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	@Override
	public String executeLocal(CoordinatorJobManager jobManager) {
		List<Connection> connections = jobManager.getFileDirectory().findAllServersWithFile(file);
		
		// request from server case
		// ServerFileJob doesnt know its target later on, fix that
		for (Connection c : connections) {
			// im assuming connections are comparable
			if (!c.equals(this.getConnection())) {
				// request from this other server
				Job forSending = new RequestJob(file.getJsonObject(), c);
				jobManager.handleNewJob(forSending);
				
				// then return
				return null;
			}
		}

		return null;
	}
	
	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.REQUEST);
		JsonObject body = file.getJsonObject();
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}
}
