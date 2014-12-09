package client;

import java.io.IOException;

public class DirectoryListenerThread implements Runnable {

	private String directoryPath;

	public DirectoryListenerThread(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public void run() {
		try {
			new DirectoryListener(directoryPath).processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
