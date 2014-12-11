package server_manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import commons.Constants;
import conn.ConnectionManager;
import server.Server;

public class ServerManager extends Server{
	// extended from server for connection commands
	
	private FileDirectory dir;
	private ServerSocket serverSocket;
	
	public static void main(String args[]) {
		// replace "server" with whatever foldername should be
		Path p = Paths.get("server");
		new ServerManager(p);
	}
	
	public ServerManager(Path path) {
		super(path);
	}

	// add a server to the server list
	public void addServer(Server s) {
		dir.addServer(s);
		// handle connection to server as well
	}
	
	public void broadcastToServers(Path p, int config) {
		// broadcasts a specific file to the servers
		// using a specific configuration defining which servers it'll send to
	}
	
	// something about disconnection and connection
}