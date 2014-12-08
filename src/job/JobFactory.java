package job;

import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

public class JobFactory {
		
	static Job createJob(JsonObject json, Socket sendingSocket) {
		String type = json.getString(Constants.JSON.TYPE);
		
		Job toGet = null;
		if (type.equals(Constants.Type.CREATE)) {
			toGet = new CreateJob(json, sendingSocket);
		
		} else if (type.equals(Constants.Type.DELETE)) {
			toGet = new DeleteJob(json, sendingSocket);
		
		} else if (type.equals(Constants.Type.FILE)) {
			toGet = new FileJob(json, sendingSocket);
		
		} else if (type.equals(Constants.Type.LIST)) {
			toGet = new ListJob(json, sendingSocket);
		
		} else if (type.equals(Constants.Type.REQUEST)) {
			toGet = new RequestJob(json, sendingSocket);
		
		} else {
			// Empty Job
		}
		
		toGet.setForReceiving();
		return toGet;
	}
}
