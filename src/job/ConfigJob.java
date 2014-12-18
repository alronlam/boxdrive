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
	
	/**
	 * 
	 * @return A configuration for new storage servers.
	 */
	public static ConfigJob getStorageServer() {
		ConfigJob job = new ConfigJob();
		job.clientType = Constants.Config.STORAGE_SERVER;
		return job;
	}
	
	
	public static ConfigJob getStorageServer(String configuration, int number) {
		ConfigJob job = new ConfigJob();
		job.clientType = Constants.Config.STORAGE_SERVER;
		job.serverConfiguration = configuration;
		job.serverId = number;
		return job;
	}
	
	
	private String clientType;
	private String serverConfiguration = ""; 
	private int serverId = -1;
	
	
	private ConfigJob() {}
	
	public String getClientType() {
		return clientType;
	}
	
	public boolean isNew() {
		return serverConfiguration.equals("") && serverId == -1;
	}
	
	public String getConfiguration() {
		return serverConfiguration;
	}
	
	public int getServerId() {
		return  serverId;
	}
	
	ConfigJob(JsonObject json) {
		JsonObject body = json.getObject(Constants.JSON.BODY);
		clientType = body.getString(Constants.Config.CLIENT_TYPE);
		
	}
	
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
		body.putNumber(Constants.Config.SERVER_NUMBER, serverId);
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}
}
