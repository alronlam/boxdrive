package job;

import java.net.Socket;

public class DeleteJob extends BasicJob {

	DeleteJob(Socket socket) {
		super(socket);
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
