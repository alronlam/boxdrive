import java.nio.file.Path;
import java.nio.file.Paths;

import server.Server;

public class ServerDriver {
	private static Server server;

	public static void main(String[] args) {
		JobManager jobManager = new JobManager();

		String sharedFolder = "server-folder";
		Path sharedFolderPath = Paths.get(sharedFolder);
		// Start-up the server
		server = new Server(sharedFolderPath, jobManager);
	}
}
