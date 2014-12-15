import java.nio.file.Path;
import java.nio.file.Paths;

import actualclient.Client;

public class ClientDriver {
	private static Client client;

	public static void main(String[] args) {
		String serverAddr = "localhost";
		String sharedFolder = "client1";
		Path sharedFolderPath = Paths.get(sharedFolder);

		// Compare the previous state with the current state of the folder and
		// then create jobs for downloading and uploading files accordingly.
		
		// Start-up the connection to the server
		client = new Client(serverAddr, sharedFolderPath);
		// call this to get socket to server, once it has connected
		client.getFirstSocket();
	}
}
