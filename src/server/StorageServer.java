package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import serverjobs.CoordinatorJobManager;
import commons.Constants;
import conn.ConnectionManager;

public class StorageServer {
	private ServerSocket serverSocket;
	private CoordinatorJobManager jobManager;

	public StorageServer(Path path) {
		Constants.FOLDER = path.toString();

		//jobManager = new ServerJobManager();
		
		try {
			serverSocket = new ServerSocket(Constants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//acceptConnections();
	}
}
