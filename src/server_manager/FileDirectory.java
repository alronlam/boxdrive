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
	
	public void addServer(Server s) {
		serverList.add(s);
		dir.put(s, new ArrayList<Path>());
	}
	
	public void updateDirectory(HashMap<Server, ArrayList<Path>> d) {
		dir = d;
	}
	
	public ArrayList<Path> getFileList(Server s) {
		return dir.get(s);
	}
	
	public int indexOfPath(Server s, Path p) {
		return dir.get(s).indexOf(p);
	}
	
	public Path getPath(Server s, int index) {
		return dir.get(s).get(index);
	}
}
