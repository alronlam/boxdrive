package job;

import org.vertx.java.core.json.JsonObject;

import client.ActualClient;
import client.Client;
import client.Connection;
import commons.Constants;

public class JobFactory {
		
	public static Job createJob(JsonObject json) {
		String type = json.getString(Constants.JSON.TYPE);
		
		Job toGet = null;
		if (type.equals(Constants.Type.CREATE)) {
			toGet = new CreateJob(json);
		
		} else if (type.equals(Constants.Type.DELETE)) {
			toGet = new DeleteJob(json);
		
		} else if (type.equals(Constants.Type.FILE)) {
			toGet = new FileJob(json);
		
		} else if (type.equals(Constants.Type.LIST)) {
			toGet = new ListJob(json);
		
		} else if (type.equals(Constants.Type.REQUEST)) {
			toGet = new RequestJob(json);
		
		} else {
			// Empty Job
		}
		
		toGet.setForReceiving();
		return toGet;
	}
	
	public static Job getConfig(Client client) {
		ConfigJob toGet = null;
		if (client instanceof ActualClient) {
			toGet = new ConfigJob();
			toGet.setAsActual();
		}
		
		return toGet;
	}
}
