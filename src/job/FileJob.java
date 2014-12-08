package job;

import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

public class FileJob extends BasicJob {
	
	FileJob(JsonObject json, Socket socket) {
		super(json, socket);
	}

	private byte[] fileBytes;

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
}
