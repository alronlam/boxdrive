import server.MultiServer;
import server.SingleServer;

public class ServerDriver {

	public static void main(String[] args) {
		// new ServerDriver().singleServer();
		new ServerDriver().multiServer();
	}
	
	private void singleServer() {
		String sharedFolder = "data/single-server";
		new SingleServer(sharedFolder);
	}
	
	private void multiServer() {
		String sharedFolder = "data/multi-server";
		new MultiServer(sharedFolder);
	}
}
