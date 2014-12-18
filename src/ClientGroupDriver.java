import java.nio.file.Path;
import java.nio.file.Paths;

import client.Client;

public class ClientGroupDriver {
	public static void main(String[] args) {
		// new Thread() {
		// public void run() {
		// ClientDriver.main(null);
		// }
		// }.start();
		//
		// new Thread() {
		// public void run() {
		// DummyClientDriver.main(null);
		// }
		// }.start();

		Client client1,client2;
		String serverAddr = "localhost";

		String sharedFolder;
		Path sharedFolderPath;
		sharedFolder = "client1";
		sharedFolderPath = Paths.get(sharedFolder);

		client1 = new Client(serverAddr, sharedFolderPath);

		sharedFolder = "client2";
		sharedFolderPath = Paths.get(sharedFolder);

		client2 = new Client(serverAddr, sharedFolderPath);
	}
}
