package job;

import java.net.Socket;

public class CreateJob extends BasicJob {

	CreateJob(Socket socket) {
		super(socket);
	}

	@Override
	public void execute() {
		// If folder, create the folder.
		
		
		// Ignore job if file exists and is newer, or has same contents.j
		
	}

}
