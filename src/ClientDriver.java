import java.nio.file.Path;
import java.nio.file.Paths;

import client.ActualClient;

public class ClientDriver {
	private static ActualClient client;

	public static void main(String[] args) {
		String serverAddr = "localhost";
		String sharedFolder = "client1";
		
		// Compare the previous state with the current state of the folder and
		// then create jobs for downloading and uploading files accordingly.
		
		new ActualClient(serverAddr, sharedFolder);
		
	}
}
