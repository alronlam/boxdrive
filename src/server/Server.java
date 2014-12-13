package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import job.JobManager;

import serverjobs.CoordinatorJobManager;
import serverjobs.ServerJobManager;
import commons.Constants;
import conn.ConnectionManager;

public class Server {
	private ServerSocket serverSocket;
	private JobManager jobManager;
	private ConnectionManager connectionManager;
	
	public static void main(String args[]) {
		new Server(Paths.get("server"));
	}

	public Server(Path path) {
		Constants.FOLDER = path.toString();

		// ServerJobManager.getInstance().setFolder(FOLDER);
		
		connectionManager = new ConnectionManager();

		// TODO: Change to ServerJobManager
		jobManager = new ServerJobManager(connectionManager);
		
		try {
			serverSocket = new ServerSocket(Constants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		acceptConnections();
	}
	
	protected void acceptConnections() {
		while (true) {
			Socket newSocket = acceptNewConnection();
			if (newSocket != null)
				connectionManager.createNewConnection(newSocket, jobManager);
			System.out.println(newSocket.getRemoteSocketAddress() + " has connected.");
		}
	}

	public Socket acceptNewConnection() {
		try {
			System.out.println("Waiting for connection.");
			Socket newSocket = serverSocket.accept();
			return newSocket;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Socket getSocket(int index) {
		return connectionManager.getSocket(index);
	}

	public Socket getFirstSocket() {
		return getSocket(0);
	}
}
