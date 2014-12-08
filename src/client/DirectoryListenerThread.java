package client;

import java.io.IOException;

public class DirectoryListenerThread implements Runnable {

	private Client client;

	public DirectoryListenerThread(Client client) {
		this.client = client;
	}

	public void run() {
		try {
			new DirectoryListener(client).processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
