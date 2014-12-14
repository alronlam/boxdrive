import java.nio.file.Path;
import java.nio.file.Paths;

import server_manager.StorageServer;

import client.Client;

public class StorageServerGroupDriver {
	public static void main(String[] args) {
		String serverAddr = "localhost";
		String sharedFolder;
		Path sharedFolderPath;
		
		sharedFolder = "storage-server1";
		sharedFolderPath = Paths.get(sharedFolder);
		StorageServer storageServer1 = new StorageServer(serverAddr, sharedFolderPath);
		
		sharedFolder = "storage-server2";
		sharedFolderPath = Paths.get(sharedFolder);
		StorageServer storageServer2 = new StorageServer(serverAddr, sharedFolderPath);
		
		sharedFolder = "storage-server3";
		sharedFolderPath = Paths.get(sharedFolder);
		StorageServer storageServer3 = new StorageServer(serverAddr, sharedFolderPath);
	}
}
