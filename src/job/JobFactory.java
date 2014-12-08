package job;

import org.vertx.java.core.json.JsonObject;

import client.AbstractClient;
import commons.Constants;

public class JobFactory {
		
	static Job createJob(JsonObject json, AbstractClient sendingClient) {
		String type = json.getString(Constants.JSON.TYPE);
		
		Job toGet = null;
		if (type.equals(Constants.Type.CREATE)) {
			toGet = new CreateJob(json, sendingClient);
		
		} else if (type.equals(Constants.Type.DELETE)) {
			toGet = new DeleteJob(json, sendingClient);
		
		} else if (type.equals(Constants.Type.FILE)) {
			toGet = new FileJob(json, sendingClient);
		
		} else if (type.equals(Constants.Type.LIST)) {
			toGet = new ListJob(json, sendingClient);
		
		} else if (type.equals(Constants.Type.REQUEST)) {
			toGet = new RequestJob(json, sendingClient);
		
		} else {
			// Empty Job
		}
		
		toGet.setForReceiving();
		return toGet;
	}
}
