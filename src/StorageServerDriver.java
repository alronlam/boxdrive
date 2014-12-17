import server.StorageServer;


public class StorageServerDriver {
	public static void main(String[] args) {
		new StorageServerDriver().multi();
	}
	
	private void multi() {
		String serverAddr = "localhost";
		String sharedFolder = "data/storage1";
		new StorageServer(serverAddr, sharedFolder);
		
		sharedFolder = "data/storage2";
		new StorageServer(serverAddr, sharedFolder);
		
		sharedFolder = "data/storage3";
		new StorageServer(serverAddr, sharedFolder);
	}
}
