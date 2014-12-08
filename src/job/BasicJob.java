package job;

import java.net.Socket;

public abstract class BasicJob extends Job {
	
	BasicJob(Socket socket) {
		super(socket);
	}

	private FileBean file;
	
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
