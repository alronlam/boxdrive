package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import commons.Constants;

import conn.ConnectionManager;


public class Client {
	static Path FOLDER;
	private Socket socket;
	private ConnectionManager connectionManager;

	public static void main(String args[])	{
		new Client("localhost",Paths.get("client1"));
	}	
	
	public Client(String serverAddr, Path path) {
		FOLDER = path;

		// ServerJobManager.getInstance().setFolder(FOLDER);
		connectionManager = new ConnectionManager();

		attemptConnection(serverAddr);
	}

	private void attemptConnection(String serverAddr) {
		do {

			try {
				socket = new Socket(serverAddr, Constants.PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socket != null) {
				connectionManager.createNewConnection(socket);
				System.out.println(socket.getRemoteSocketAddress() + " has connected.");
			}
		} while (socket == null);
	}

	public Socket getSocket(int index) {
		return connectionManager.getSocket(index);
	}

	public Socket getFirstSocket() {
		return getSocket(0);
	}
	
	
	
//	public void readFromServer(){
//		connectionManager
//	}
}
