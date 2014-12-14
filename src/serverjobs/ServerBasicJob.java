package serverjobs;

import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import conn.Connection;
import job.FileBean;


public abstract class ServerBasicJob extends ServerJob {
	protected FileBean file;
	
	
	/**
	 * Clone constructor.
	 * @param job
	 */
	ServerBasicJob(ServerBasicJob job) {
		super(job.getConnection());
		this.file = new FileBean(job.file);
	}
	
	ServerBasicJob(Path path, Connection connection) {
		super(connection);
		file = new FileBean(path);
	}
	

	ServerBasicJob(JsonObject json, Connection connection) {
		super(connection);
		JsonObject body = json.getObject(Constants.JSON.BODY);
		file = new FileBean(body);
	}

}
