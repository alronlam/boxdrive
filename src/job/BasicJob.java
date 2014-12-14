package job;

import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import conn.Connection;
import filemanager.FileBean;

public abstract class BasicJob extends Job {
	public FileBean file;
	
	
	/**
	 * Clone constructor.
	 * @param job
	 */
	BasicJob(BasicJob job) {
		super(job.getConnection());
		this.file = new FileBean(job.file);
	}
	
	BasicJob(FileBean file, Connection connection) {
		super(connection);
		this.file = file;
	}
	

	BasicJob(JsonObject json, Connection connection) {
		super(connection);
		JsonObject body = json.getObject(Constants.JSON.BODY);
		file = new FileBean(body);
	}
}
