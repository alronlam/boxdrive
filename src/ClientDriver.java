import client.DirectoryListenerThread;

public class ClientDriver {

	public static void main(String[] args) {
		new DirectoryListenerThread("shared-folder");
	}

}
