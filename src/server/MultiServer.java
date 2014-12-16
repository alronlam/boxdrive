package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.vertx.java.core.json.JsonObject;

import server.manager.StorageServerManager;
import server.manager.TriangleServerManager;
import job.ConfigJob;
import job.JobFactory;
import job.manager.ServerJobManager;
import client.Client;
import client.Connection;
import commons.Constants;
import file.FileManager;
import file.MultiServerFileManager;
import file.SingleFolderFileManager;

public class MultiServer {
	private ServerSocket serverSocket;
	private ServerJobManager jobManager;
	
	private FileManager fileManager;
	private StorageServerManager storageServerManager;
	private ClientManager clientManager;
	
	
	public MultiServer(String localFolder) {
		storageServerManager = new TriangleServerManager();
		fileManager = new MultiServerFileManager(storageServerManager);
		clientManager = new ClientManager();
		
		jobManager = new ServerJobManager(fileManager, clientManager);
		
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
				Connection connection = new Connection(newSocket);
				Client client = new Client(connection, jobManager);
				clientManager.add(client);
				client.listenForJobs();
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
	
	private void handleNewClient(Client client) {
		String json = client.getConnection().read();
		ConfigJob job = (ConfigJob) JobFactory.createJob(new JsonObject(json));
		
		
	}
}
