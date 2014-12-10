package server_manager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import server.Server;

public class FileDirectory {
	private ArrayList<Server> serverList;
	private HashMap<Server, ArrayList<Path>> dir;
	
	public ArrayList<Server> getServerList() {
		return serverList;
	}
	
	// for initializing a new server
	public void addServer(Server s) {
		serverList.add(s);
		dir.put(s, new ArrayList<Path>());
	}
	
	/** 
	 * If the ArrayList of files already exist for the server, just dump it here.
	 * @param s
	 * @param list
	 * @return
	 */
	public void setServerDirectory(Server s, ArrayList<Path> list) {
		ArrayList<Path> currList = dir.get(s);
		currList.clear();
		currList.addAll(list);
	}
	
	// actually needed stuff
	public ArrayList<Path> getFileList(Server s) {
		return dir.get(s);
	}
	
	/**
	 * Finds first server that stores Path p in the hash map.
	 * Returns null if file not found.
	 * Use this for retrieving server files for a client.
	 * @param p
	 * @return
	 */
	public Server findServerThatHasFile(Path p) {
		for (Server s : serverList) {
			if (dir.get(s).contains(p))
				return s;
		}
		
		return null;
	}
	
	/**
	 * Basically the findServer function but returns all of them in a list.
	 * Use this for updating server files.
	 * @param p
	 * @return
	 */
	public ArrayList<Server> findAllServersWithFile(Path p) {
		ArrayList<Server> out = new ArrayList<Server>();
		
		for (Server s : serverList) {
			if (dir.get(s).contains(p))
				out.add(s);
		}
		
		return out;
	}
	
	// utility stuff
	public int indexOfPath(Server s, Path p) {
		return dir.get(s).indexOf(p);
	}
	
	public Path getPath(Server s, int index) {
		return dir.get(s).get(index);
	}
}
