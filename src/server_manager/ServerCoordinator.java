package server_manager;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import server.Server;

public class ServerCoordinator extends Server{
	// extended from server for connection commands
	
	private FileDirectory dir;
	private ServerSocket serverSocket;
	
	public static void main(String args[]) {
		Path p = Paths.get("server");
		new ServerCoordinator(p);
	}
	
	public ServerCoordinator(Path path) {
		super(path);
	}
	
	@Override
	public Socket acceptNewConnection() {
		// how does one differentiate a server and client?
		
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