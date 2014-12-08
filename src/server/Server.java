package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import commons.Constants;

import conn.ConnectionManager;


public class Server {
	static Path FOLDER;
	private ServerSocket serverSocket;
	private ConnectionManager connectionManager;
	
	public static void main(String args[])	{
		new Server(Paths.get("server"));
	}	
	
	
	public Server(Path path) {
		FOLDER = path;
		
		//ServerJobManager.getInstance().setFolder(FOLDER);
		
				try {
			serverSocket = new ServerSocket(Constants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		acceptConnections();
	}

	private void acceptConnections(){
		while (true) {
			Socket newSocket = acceptNewConnection();
			if(newSocket != null)
				connectionManager.getInstance().createNewConnection(newSocket);
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
