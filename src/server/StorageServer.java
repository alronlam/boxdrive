package server;

import java.io.IOException;
import java.net.Socket;

import job.manager.StorageServerJobManager;
import client.Client;
import client.Connection;

import commons.Constants;

import file.SingleFolderFileManager;

public class StorageServer extends Client {
	private SingleFolderFileManager fileManager;
	private ConfigManager configManager;
	
	public StorageServer(String serverAddr, String localFolder) {
		System.out.println("storage server initialized");
		this.fileManager = new SingleFolderFileManager(localFolder);
		this.configManager = new ConfigManager(this, localFolder);
		this.setJobManager(new StorageServerJobManager(fileManager, configManager));
		
		this.setConnection( this.attemptConnection(serverAddr));
		this.listenForJobs();
		this.configManager.sendConfig();
	}
	
	private Connection attemptConnection(String serverAddr) {
		Socket socket = null;
		do {
			System.out.println("Storage server trying to connect;");
			try {
				socket = new Socket(serverAddr, Constants.PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socket != null) {
				Connection connection = new Connection(socket);
				System.out.println(socket.getRemoteSocketAddress() + " has connected.");
				return connection;
			}
		} while (socket == null);
		return null;
	}
}
