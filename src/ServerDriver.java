import server.SingleServer;

public class ServerDriver {

	public static void main(String[] args) {
		new ServerDriver().singleServer();
	}
	
	private void singleServer() {
		String sharedFolder = "data/single-server";
		new SingleServer(sharedFolder);
	}
}
