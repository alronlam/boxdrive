package job;

import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

public class CreateJob extends BasicJob {

	CreateJob(JsonObject json, Socket socket) {
		super(json, socket);
	}

	@Override
	public void execute() {
		// If folder, create the folder.
		
		
		// Ignore job if file exists and is newer, or has same contents.j
		
	}

}
