package server.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.Client;
import file.FileBean;

public abstract class StorageServerManager {
	
	
	public abstract void addServer(Client client);
	public abstract void addServer(Client client, int server);
	public abstract void deleteServer(Client client);

	
	public abstract int getNewVirtualServer();
	public abstract boolean createFile(FileBean file, String fileBytes, int server);
	public abstract boolean createDirectory(FileBean file, int server);
	public abstract void delete(FileBean file, int server);
	public abstract String getFileBytes(FileBean file, int server);
	
	
}
