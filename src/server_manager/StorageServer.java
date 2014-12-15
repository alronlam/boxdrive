package server_manager;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import client.Connection;
import client.ConnectionManager;
import job.manager.JobManager;
import serverjobs.CoordinatorJobManager;
import serverjobs.ServerJobManager;
import commons.Constants;


public class StorageServer {
	private Socket socket;
	private JobManager jobManager;
	private ConnectionManager connectionManager;
	public int storageGroup;

	public static void main(String args[])	{
		new StorageServer("localhost",Paths.get("client1"));
	}	
	
	public StorageServer(String serverAddr, Path path) {
		Constants.FOLDER = path.toString(); // Makeshift global. Bad.
		
		// ServerJobManager.getInstance().setFolder(FOLDER);
		
		connectionManager = new ConnectionManager();
		
		// TODO: Change to ServerJobManager
		jobManager = new ServerJobManager(connectionManager);

		Connection conn = attemptConnection(serverAddr);
		//new Thread(new DirectoryListenerThread(path, conn, jobManager)).start();;
	}

	private Connection attemptConnection(String serverAddr) {
		do {

			try {
				socket = new Socket(serverAddr, Constants.COORDINATOR_PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socket != null) {
				Connection connection = connectionManager.createNewConnection(socket,jobManager);
				System.out.println(socket.getRemoteSocketAddress() + " has connected.");
				return connection;
			}
		} while (socket == null);
		return null;
	}

	public Socket getSocket(int index) {
		return connectionManager.getSocket(index);
	}

	public Socket getFirstSocket() {
		return getSocket(0);
	}
}
