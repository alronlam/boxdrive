package job;

import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

public class RequestJob extends BasicJob {

	RequestJob(JsonObject json, Socket socket) {
		super(json, socket);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
