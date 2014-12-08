package job;

import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

import util.Constants;

public class JobFactory {
	// JSON Fields
	private final String JSON_TYPE = "Type";
	public static final String JSON_BODY = "Body";
	
	// Job Types
	private final String CREATE_TYPE = "Create";
	private final String DELETE_TYPE = "Delete";
	private final String FILE_TYPE = "File";
	private final String LIST_TYPE = "List";
	private final String REQUEST_TYPE = "Request";
	
	
	
	Job get(JsonObject json, Socket sendingSocket) {
		String type = json.getString(JSON_TYPE);
		
		Job toGet = null;
		if (type.equals(CREATE_TYPE)) {
			toGet = new CreateJob(json, sendingSocket);
		
		} else if (type.equals(DELETE_TYPE)) {
			toGet = new DeleteJob(json, sendingSocket);
		
		} else if (type.equals(FILE_TYPE)) {
			toGet = new FileJob(json, sendingSocket);
		
		} else if (type.equals(LIST_TYPE)) {
			toGet = new ListJob(json, sendingSocket);
		
		} else if (type.equals(REQUEST_TYPE)) {
			toGet = new RequestJob(json, sendingSocket);
		
		} else {
			// Empty Job
		}
		
		return toGet;
	}
}
