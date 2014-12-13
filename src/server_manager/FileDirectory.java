package server_manager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import conn.Connection;

import server.Server;

public class FileDirectory {
	private Map<Connection, Integer> serverConfig;
	private Map<Connection, List<FileObject>> fileAssignment;
	public List<FileObject> fileConfig;

	private int runningVal = 0;

	// public List<Connection> getServerList() {
	// return serverList;
	// }

	// for initializing a new server
	public boolean addServer(Connection conn) {
		if (!serverConfig.containsKey(conn)) {
			serverConfig.put(conn, runningVal);
			fileAssignment.put(conn, new ArrayList<FileObject>());

			runningVal = (runningVal + 1) % 3;
			return true;
		}
		return false;
	}

	/**
	 * If the ArrayList of files already exist for the server, just dump it here.
	 * 
	 * @param s
	 * @param list
	 * @return
	 */
	public void setServerDirectory(Connection s, List<FileObject> list) {
		List<FileObject> currList = fileAssignment.get(s);
		currList.clear();
		currList.addAll(list);
	}

	// actually needed stuff
	public List<FileObject> getFileList(Connection s) {
		return fileAssignment.get(s);
	}

	/**
	 * Finds first server that stores Path p in the hash map. Returns null if file not found. Use this for retrieving server files for a client.
	 * 
	 * @param p
	 * @return
	 */
	// public Connection findServerThatHasFile(FileObject p) {
	// for (Connection s : serverList) {
	// if (fileAssignment.get(s).contains(p))
	// return s;
	// }
	//
	// return null;
	// }

	/**
	 * Basically the findServer function but returns all of them in a list. Use this for updating server files.
	 * 
	 * @param p
	 * @return
	 */
	// public List<Connection> findAllServersWithFile(FileObject p) {
	// List<Connection> out = new ArrayList<>();
	//
	// for (Connection s : serverList) {
	// if (fileAssignment.get(s).contains(p))
	// out.add(s);
	// }
	//
	// return out;
	// }

	// utility stuff
	public int indexOfPath(Connection s, FileObject p) {
		return fileAssignment.get(s).indexOf(p);
	}

	public FileObject getPath(Connection s, int index) {
		return fileAssignment.get(s).get(index);
	}
}
