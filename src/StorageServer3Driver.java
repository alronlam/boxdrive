import java.nio.file.Path;
import java.nio.file.Paths;

import server_manager.StorageServer;


public class StorageServer3Driver {
	public static void main(String[] args) {
		String serverAddr = "localhost";
		String sharedFolder;
		Path sharedFolderPath;
		
		sharedFolder = "storage-server3";
		sharedFolderPath = Paths.get(sharedFolder);
		StorageServer storageServer1 = new StorageServer(serverAddr, sharedFolderPath);
	}
}
