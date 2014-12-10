package server_manager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import server.Server;

public class ServerManager {
	private FileDirectory dir;
	
	// add a server to the server list
	public void addServer(Server s) {
		dir.addServer(s);
	}
	
	public void broadcastToServers(Path p, int config) {
		// broadcasts a specific file to the servers
		// using a specific configuration defining which servers it'll send to
	}
	
	// something about disconnection and connection
}
