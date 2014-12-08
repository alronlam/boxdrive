package job;

import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

import util.Constants;

public abstract class BasicJob extends Job {
	private FileBean file;
	
	BasicJob(JsonObject json, Socket socket) {
		super(socket);
		JsonObject body = json.getObject(Constants.JSON.BODY);
		file = new FileBean(body);
	}
	
	protected String getFilename() {
		return file.getFilename();
	}
	
	protected long getLastModified() {
		return file.getLastModified();
	}
	
	protected byte[] getChecksum() {
		return file.getChecksum();
	}
	
	protected boolean isDirectory() {
		return file.isDirectory();
	}
}
