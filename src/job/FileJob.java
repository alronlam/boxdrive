package job;

import java.net.Socket;

public class FileJob extends BasicJob {
	
	FileJob(Socket socket) {
		super(socket);
	}

	private byte[] fileBytes;

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
}
