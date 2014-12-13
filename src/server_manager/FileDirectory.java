package server_manager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import job.FileBean;
import job.Job;

import conn.Connection;

import server.Server;

public class FileDirectory {
	private Map<Connection, Integer> serverConfig;
	private Map<FileBean, Integer> fileConfig;
	// private Map<Connection, List<FileObject>> fileAssignment;
	// public List<FileObject> fileConfig;

	// TODO: possibly make a method in FileBean that just checks if the filename is equal, 
	// as the key pairing in the map may be affected due to the modified time

	private int runningValServer = 0;
	private int runningValFile = 0;
	private int STORAGE_SERVER_GROUPS = 3;

	// public List<Connection> getServerList() {
	// return serverList;
	// }

	public FileDirectory() {
		serverConfig = new HashMap<>();
		// for(int i = 0; i < STORAGE_SERVER_GROUPS; ++i)
		// serverConfig.put(i, new ArrayList<Connection>());

		fileConfig = new HashMap<>();
		// for(int i = 0; i < STORAGE_SERVER_GROUPS; ++i)
		// fileConfig.put(i, new ArrayList<FileBean>());
	}

	// for initializing a new server
	public boolean addServer(Connection conn) {
		if (!serverConfig.containsKey(conn)) {
			// serverConfig.get(runningVal).add(conn);
			serverConfig.put(conn, runningValServer);
			// fileAssignment.put(conn, new ArrayList<FileObject>());

			runningValServer = (runningValServer + 1) % STORAGE_SERVER_GROUPS;
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
	// public void setServerDirectory(Connection s, List<FileObject> list) {
	// List<FileObject> currList = fileAssignment.get(s);
	// currList.clear();
	// currList.addAll(list);
	// }

	// actually needed stuff
	// public List<FileObject> getFileList(Connection s) {
	// return fileAssignment.get(s);
	// }

	public List<Connection> getServersFromConfig(int config) {
		List<Connection> out = new ArrayList<Connection>();
		
		for (Connection c : serverConfig.keySet())
			if (serverConfig.get(c) == config)
				out.add(c);
		
		if (!out.isEmpty())
			return out;
		
		return null;
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
	public List<Connection> findAllServersWithFile(FileBean file) {

		int config = fileConfig.get(file);

		return findAllServersWithConfig(config);
	}

	public List<Connection> findAllServersWithConfig(int config) {
		List<Connection> out = new ArrayList<>();

		for (Connection conn : serverConfig.keySet()) {
			if (!serverConfig.get(conn).equals(config))
				out.add(conn);
		}

		return out;
	}

	public List<Connection> getServerListForFile(FileBean file) {
		List<Connection> out;

		if (fileConfig.containsKey(file))
			out = findAllServersWithConfig(fileConfig.get(file));
		else {
			out = findAllServersWithConfig(runningValFile);
			fileConfig.put(file, runningValFile);
			runningValFile = (runningValFile + 1) % this.STORAGE_SERVER_GROUPS;
		}

		return out;
	}

	// utility stuff
	// public int indexOfPath(Connection s, FileObject p) {
	// return fileAssignment.get(s).indexOf(p);
	// }

	// public FileObject getPath(Connection s, int index) {
	// return fileAssignment.get(s).get(index);
	// }
}
