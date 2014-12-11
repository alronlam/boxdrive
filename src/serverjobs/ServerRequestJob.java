package serverjobs;

import java.nio.file.Files;
import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import conn.Connection;

public class ServerRequestJob extends ServerBasicJob {
	
	/**
	 * Creates a Request with the same parameters as the job argument.
	 * @param job
	 */
	ServerRequestJob(ServerBasicJob job) {
		super(job);
	}
	
	ServerRequestJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	@Override
	public String executeLocal(ServerJobManager jobManager) {
		// stub
		return null;
		/*
		Path localFile = file.getLocalizedFile();
		
		if (!Files.exists(localFile)) {
			return null;
		}
		
		Job forSending = new FileJob(localFile, this.getConnection());
		jobManager.handleNewJob(forSending);
		
		return null;
		*/
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
