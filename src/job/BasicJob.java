package job;

import java.net.Socket;
import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

public abstract class BasicJob extends Job {
	protected FileBean file;
	
	
	/**
	 * Clone constructor.
	 * @param job
	 */
	BasicJob(BasicJob job) {
		super(job.getSocket());
		this.file = new FileBean(file);
	}
	
	BasicJob(Path path, Socket socket) {
		super(socket);
		file = new FileBean(path);
	}
	
	BasicJob(JsonObject json, Socket socket) {
		super(socket);
		JsonObject body = json.getObject(Constants.JSON.BODY);
		file = new FileBean(body);
	}
}
