import java.nio.file.Path;
import java.nio.file.Paths;

import server.Server;
import server_manager.ServerCoordinator;

public class ServerCoordinatorDriver {
	private static ServerCoordinator sm;

	public static void main(String[] args) {
		/*
		String sharedFolder = "server-folder";
		Path sharedFolderPath = Paths.get(sharedFolder);
		// Start-up the server
		sm = new ServerManager(sharedFolderPath);
		*/

		// start servermanager
		// start 3 servers
		// connect servers to servermanager
		
		String sharedFolder = "server";
		Path sharedFolderPath = Paths.get(sharedFolder);
		// Start-up the server
		new ServerCoordinator(sharedFolderPath);
	}
}
