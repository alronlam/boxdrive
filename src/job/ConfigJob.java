package job;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import file.FileManager;

/**
 * Helper Job when initializing clients. 
 *
 */
public class ConfigJob extends Job {
	public static ConfigJob getActual() {
		ConfigJob job = new ConfigJob();
		job.clientType = Constants.Config.ACTUAL;
		return job;
	}
	
	public static ConfigJob getServer() {
		ConfigJob job = new ConfigJob();
		job.clientType = Constants.Config.ACTUAL;
		return job;
	}
	
	
	private String clientType;
	private String serverConfiguration = ""; 
	private int serverNumber = -1;
	
	
	private ConfigJob() {}
	
	/**
	 * Does nothing.
	 */
	@Override
	public Job execute(FileManager filemanager) {
		return null;
	}

	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.CONFIG);
		JsonObject body = new JsonObject();
		body.putString(Constants.Config.CLIENT_TYPE, clientType);
		body.putString(Constants.Config.SERVER_CONFIGURATION, serverConfiguration);
		body.putNumber(Constants.Config.SERVER_NUMBER, serverNumber);
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}
	
	
	void setVirtualServer(int serverNumber) {
		this.serverNumber = serverNumber;
	}
}
