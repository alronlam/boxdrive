package job;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

public class RequestJob extends BasicJob {
	
	/**
	 * Creates a Request with the same parameters as the job argument.
	 * @param job
	 */
	RequestJob(BasicJob job) {
		super(job);
	}
	
	RequestJob(JsonObject json, Socket socket) {
		super(json, socket);
	}

	@Override
	public void execute() {
		Path localFile = file.getLocalizedFile();
		
		if (!Files.exists(localFile)) {
			return;
		}
		
		Job forSending = new FileJob(localFile, this.getSocket());
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
