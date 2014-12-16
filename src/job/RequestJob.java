package job;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import file.FileBean;
import file.FileManager;

public class RequestJob extends BasicJob {
	
	/**
	 * Creates a Request with the same parameters as the job argument.
	 * @param job
	 */
	public RequestJob(BasicJob job) {
		super(job);
	}
	
	public RequestJob(JsonObject json) {
		super(json);
	}
	
	public RequestJob(FileBean file) {
		super(file);
	}
	
	@Override
	public Job execute(FileManager filemanager) {
		
		if (!filemanager.exists(file)) {
			return null;
		}
		
		Job forSending = new FileJob(file, filemanager);
		return forSending;
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
