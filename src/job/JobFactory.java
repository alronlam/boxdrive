package job;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import conn.Connection;

public class JobFactory {

	static Job createJob(JsonObject json, Connection connection) {
		String type = json.getString(Constants.JSON.TYPE);

		Job toGet = null;
		if (type.equals(Constants.Type.CREATE)) {
			toGet = new CreateJob(json, connection);

		} else if (type.equals(Constants.Type.DELETE)) {
			toGet = new DeleteJob(json, connection);

		} else if (type.equals(Constants.Type.FILE)) {
			toGet = new FileJob(json, connection);

		} else if (type.equals(Constants.Type.LIST)) {
			toGet = new ListJob(connection);

		} else if (type.equals(Constants.Type.REQUEST)) {
			toGet = new RequestJob(json, connection);

		} else if (type.equals(Constants.Type.LIST_RESULT)) {
			toGet = new ListResultJob(json, connection);

		} else {
			// Empty Job
		}

		toGet.setForReceiving();
		return toGet;
	}
}
