package server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import job.ConfigJob;
import job.CreateJob;
import job.DeleteJob;
import job.FileJob;
import job.Job;
import job.RequestJob;
import client.Client;
import file.FileBean;

public class TriangleServerManager extends StorageServerManager {
	private final String CONFIGURATION = "triangle";
	private final int MAX_SERVERS = 3;
	private int currentServer = 0;
	private Map<Integer, List<Client>> serverMap = new HashMap<>();
	
	public TriangleServerManager() {
		List<Client> zero = new ArrayList<>();
		List<Client> one = new ArrayList<>();
		List<Client> two = new ArrayList<>();
		
		serverMap.put(0, zero);
		serverMap.put(1, one);
		serverMap.put(2, two);
	}
	
	@Override
	public void addNewServer(Client client) {
		// Find virtual server with least servers
		int serverNumber = vsWithLeastServers();
		serverMap.get(serverNumber).add(client);
		serverMap.get((serverNumber + 1) % MAX_SERVERS).add(client); // Add to succeeding
		ConfigJob config = ConfigJob.getStorageServer(CONFIGURATION, serverNumber);
		client.getConnection().write(config.getJson());

		// Copy files from existing servers
	}
	
	private int vsWithLeastServers() {
		int maxSize = -1;
		int server = 0;
		for (int currentServer : serverMap.keySet()) {
			int currentSize = serverMap.get(currentServer).size();
			if (currentSize > maxSize) {
				server = currentServer; 
			}
		}
		
		return server;
	}
	
	@Override
	public void addServer(Client client, int server) {
		List<Client> serverList = serverMap.get(server);
		serverList.add(client);
	}

	@Override
	public void deleteServer(Client client) {
		for (int currentServer : serverMap.keySet()) {
			serverMap.get(currentServer).remove(client);
		}
	}

	@Override
	public int getNewServerNumber() {
		return (currentServer++ % MAX_SERVERS);
	}

	@Override
	public boolean createFile(FileBean file, String fileBytes, int serverNumber) {
		FileJob job = new FileJob(file, fileBytes);
		return this.sendToAllServers(job, serverNumber);
	}

	@Override
	public boolean createDirectory(FileBean file, int serverNumber) {
		CreateJob job = new CreateJob(file);
		return this.sendToAllServers(job, serverNumber);
	}

	@Override
	public boolean delete(FileBean file, int serverNumber) {
		DeleteJob job = new DeleteJob(file, file.getLastModified());
		return this.sendToAllServers(job, serverNumber);
	}

	@Override
	public String getFileBytes(FileBean file, int serverId) {
		Client server = this.getRandomServer(serverId);
		if (server == null) {
			return null;
		}
		
		// Get file from a random storage server
		RequestJob requestJob = new RequestJob(file);
		server.getConnection().write(requestJob.getJson());
		
		// Read immediately. Could be dangerous.
		String jsonStr = null;
		while (jsonStr == null) {
			jsonStr = server.getConnection().read();
		}
		
		// A very strong assumption. Trust in the protocol.
		JsonObject json = new JsonObject(jsonStr);
		return json.getObject(Constants.JSON.BODY).getString(Constants.Body.FILEBYTES);
	}
	
	private boolean sendToAllServers(Job job, int serverId) {
		List<Client> servers = this.getAllServers(serverId);
		if (servers.isEmpty()) {
			return false;
		}
		
		for (Client server : servers) {
			server.getConnection().write(job.getJson());
		}
		
		return true;
	}

	private Client getRandomServer(int serverId) {
		List<Client> servers = this.getAllServers(serverId);
		if (servers.isEmpty()) {
			return null;
		}
		
		Random rn = new Random();
		int randomIndex = rn.nextInt(servers.size());
		return servers.get(randomIndex);
	}
	
	private List<Client> getAllServers(int serverId) {
		List<Client> servers = new ArrayList<>();
		servers.addAll(serverMap.get(serverId));
		servers.addAll(serverMap.get((serverId + 1) % MAX_SERVERS));
		return servers;
	}
}
