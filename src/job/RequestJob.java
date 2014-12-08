package job;

import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

public class RequestJob extends BasicJob {
	
	/**
	 * Creates a Request with the same parameters as the job argument.
	 * @param job
	 */
	RequestJob(BasicJob job) {
		super(job);
	}
	
	RequestJob(JsonObject json, Socket socket) {
		super(json, socket);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
}
