package serverjobs;

import org.vertx.java.core.json.JsonObject;

import client.Connection;
import commons.Constants;

public class ServerJobFactory {
		
	static ServerJob createJob(JsonObject json, Connection connection) {
		String clientJobType = json.getString(Constants.JSON.TYPE);
		
		ServerJob toGet = null;
		
		// this is temporary, need to make another set of commands for the server
		// probably need to understand the jobs fully na rin
		
		switch (clientJobType) {
			case Constants.Type.CREATE:
				// toGet = new 
				break;
				
			case Constants.Type.DELETE:
				break;

			case Constants.Type.FILE:
				break;

			case Constants.Type.LIST:
				break;

			case Constants.Type.REQUEST:
				break;
		}
		
		/* why cant this be a switch again?
		if (type.equals(Constants.Type.CREATE)) {
			toGet = new CreateJob(json, connection);
		
		} else if (type.equals(Constants.Type.DELETE)) {
			toGet = new DeleteJob(json, connection);
		
		} else if (type.equals(Constants.Type.FILE)) {
			toGet = new FileJob(json, connection);
		
		} else if (type.equals(Constants.Type.LIST)) {
			toGet = new ListJob(json, connection);
		
		} else if (type.equals(Constants.Type.REQUEST)) {
			toGet = new RequestJob(json, connection);
		
		} else {
			// Empty Job
		}
		*/
		
		toGet.setForReceiving();
		return toGet;
	}
}
