package job;

import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

public class DeleteJob extends BasicJob {

	DeleteJob(JsonObject json, Socket socket) {
		super(json, socket);
	}

	@Override
	public void execute() {
		// Return if not exists.
		
		// Check if deletion is newer than existing file.
		
		// If newer, then safe to delete.
		
		// If not, file in this system is newer.
		// Send a Create job to server.
	}

}
