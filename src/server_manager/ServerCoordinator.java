package server_manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import serverjobs.ServerJobManager;
import commons.Constants;
import conn.ConnectionManager;

public class ServerCoordinator {
	private ServerSocket serverSocketClient, serverSocketStorageServer;
	private ServerJobManager jobManager;
	private ConnectionManager connMgrClients, connMgrStorageServers;

	public static void main(String args[]) {
		new ServerCoordinator(Paths.get("server"));
	}

	public ServerCoordinator(Path path) {
		Constants.FOLDER = path.toString();

		// ServerJobManager.getInstance().setFolder(FOLDER);

		connMgrClients = new ConnectionManager();
		connMgrStorageServers = new ConnectionManager();

		// TODO: Change to ServerJobManager
		jobManager = new ServerJobManager(connMgrClients, connMgrStorageServers);

		try {
			serverSocketStorageServer = new ServerSocket(Constants.COORDINATOR_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		new Thread() {
			public void run() {
				acceptConnections(connMgrStorageServers, serverSocketStorageServer);
			}
		}.start();

		try {
			serverSocketClient = new ServerSocket(Constants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		acceptConnections(connMgrClients, serverSocketClient);
	}

	protected void acceptConnections(ConnectionManager connMgr, ServerSocket serverSocket) {
		while (true) {
			Socket newSocket = acceptNewConnection(serverSocket);
			if (newSocket != null) {
				connMgr.createNewConnection(newSocket, jobManager);
				jobManager.updateFileDirectory();
			}
			System.out.println(newSocket.getRemoteSocketAddress() + " has connected.");
		}
	}

	public Socket acceptNewConnection(ServerSocket serverSocket) {
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
		return connMgrClients.getSocket(index);
	}

	public Socket getFirstSocket() {
		return getSocket(0);
	}
}
