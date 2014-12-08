package job;

import java.net.Socket;
import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import client.AbstractClient;
import commons.Constants;

public abstract class BasicJob extends Job {
	protected FileBean file;
	
	
	/**
	 * Clone constructor.
	 * @param job
	 */
	BasicJob(BasicJob job) {
		super(job.getClient());
		this.file = new FileBean(file);
	}
	
	BasicJob(Path path, AbstractClient client) {
		super(client);
		file = new FileBean(path);
	}
	

	BasicJob(JsonObject json, AbstractClient client) {
		super(client);
		JsonObject body = json.getObject(Constants.JSON.BODY);
		file = new FileBean(body);
	}
}
