package job;

import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import conn.Connection;

public abstract class BasicJob extends Job {
	protected FileBean file;
	
	
	/**
	 * Clone constructor.
	 * @param job
	 */
	BasicJob(BasicJob job) {
		super(job.getConnection());
		this.file = new FileBean(file);
	}
	
	BasicJob(Path path, Connection connection) {
		super(connection);
		file = new FileBean(path);
	}
	

	BasicJob(JsonObject json, Connection connection) {
		super(connection);
		JsonObject body = json.getObject(Constants.JSON.BODY);
		file = new FileBean(body);
	}
}
