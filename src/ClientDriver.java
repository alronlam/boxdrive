import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import job.JobManager;

import client.Client;
import client.DirectoryListenerThread;

public class ClientDriver {
	private static Client client;

	public static void main(String[] args) {
		JobManager jobManager = new JobManager();

		String serverAddr = "localhost";
		String sharedFolder = "shared-folder";
		Path sharedFolderPath = Paths.get(sharedFolder);

		// Compare the previous state with the current state of the folder and
		// then create jobs for downloading and uploading files accordingly.

		// Start-up the connection to the server
		client = new Client(serverAddr, sharedFolderPath, jobManager);
		// call this to get socket to server, once it has connected
		client.getFirstSocket();

		// Start up the directory listener
		new DirectoryListenerThread("shared-folder");
	}
}
