import client.DirectoryListenerThread;

public class ClientDriver {

	public static void main(String[] args) {

		// Compare the previous state with the current state of the folder and
		// then create jobs for downloading and uploading files accordingly.

		// Start-up the connection to the server

		// Start up the directory listener
		new DirectoryListenerThread("shared-folder");
	}

}
