package job;

import java.nio.file.Files;
import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import conn.Connection;

public class RequestJob extends BasicJob {
	
	/**
	 * Creates a Request with the same parameters as the job argument.
	 * @param job
	 */
	RequestJob(BasicJob job) {
		super(job);
	}
	
	RequestJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	@Override
	public void executeLocal() {
		Path localFile = file.getLocalizedFile();
		
		if (!Files.exists(localFile)) {
			return;
		}
		
		Job forSending = new FileJob(localFile, this.getConnection());
		JobManager.getInstance().handleNewJob(forSending);
		
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
