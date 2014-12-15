import client.ActualClient;

public class ClientDriver {
	
	public static void main(String[] args) {
		String serverAddr = "localhost";
		String sharedFolder = "data/client1";
		new ActualClient(serverAddr, sharedFolder);
		
		sharedFolder = "data/client2";
		new ActualClient(serverAddr, sharedFolder);
		
	}
}
