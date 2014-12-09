package client;

import java.io.IOException;
import java.nio.file.Path;

import conn.Connection;

public class DirectoryListenerThread implements Runnable {

	private Path directoryPath;
	private Connection connection;
	
	public DirectoryListenerThread(Path directoryPath, Connection connection) {
		this.directoryPath = directoryPath;
		this.connection = connection;
	}

	public void run() {
		try {
			new DirectoryListener(directoryPath, connection).processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
