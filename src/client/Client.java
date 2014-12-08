package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import job.JobManager;

import commons.Constants;

import conn.ConnectionManager;


public class Client extends AbstractClient {
	private final Path folder;
	
	public static void main(String args[])	{
		new Client("localhost",Paths.get("client1"));
	}	
	
	public Client(String serverAddr, Path path) {
		folder = path;
		attemptConnection(serverAddr);
	}

	private void attemptConnection(String serverAddr) {
		Socket socket = null;
		do {
			try {
				socket = new Socket(serverAddr, Constants.PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socket != null) {
				this.getConnectionManager().createNewConnection(socket);
				System.out.println(socket.getRemoteSocketAddress() + " has connected.");
			}
		} while (socket == null);
	}
	
	public Path getFolder() {
		return folder;
	}
}
