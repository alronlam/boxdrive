package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import client.Client;
import client.Connection;
import job.manager.JobManager;
import serverjobs.CoordinatorJobManager;
import serverjobs.ServerJobManager;
import commons.Constants;
import file.SingleFolderFileManager;

public class SingleServer {
	private ServerSocket serverSocket;
	private JobManager jobManager;
	private SingleFolderFileManager fileManager;
	private ActualClientManager clientManager;
	
	
	public SingleServer(String localFolder) {
		clientManager = new ActualClientManager();
		fileManager = new SingleFolderFileManager(localFolder);
		
		jobManager = new ServerJobManager(fileManager);
		
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
			if (newSocket != null) {
				Client client = new Client();
				Connection connection = new Connection(client, newSocket);
				connection.read(); // Read out the configuration. Assumed to be ActualClient.
				clientManager.add(client);
			}
				
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

}
