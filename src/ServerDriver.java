import java.nio.file.Path;
import java.nio.file.Paths;

import server.Server;

public class ServerDriver {

	public static void main(String[] args) {
		String sharedFolder = "server";
		Path sharedFolderPath = Paths.get(sharedFolder);
		// Start-up the server
		new Server(sharedFolderPath);
	}
}
