package job;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import file.FileManager;

/**
 * Helper Job when initializing clients. 
 *
 */
public class ConfigJob extends Job {
	private String clientType;
	private String virtualServer = "";
	
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
		body.putString(Constants.Config.VIRTUAL_SERVER, virtualServer);
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}
	
	void setAsActual() {
		clientType = Constants.Config.ACTUAL;
	}
	
	void setVirtualServer(String virtualServer) {
		this.virtualServer = virtualServer;
	}
}
