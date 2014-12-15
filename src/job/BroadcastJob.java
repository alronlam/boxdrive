package job;

import java.util.ArrayList;
import java.util.List;

import conn.Client;
import filemanager.FileManager;

public class BroadcastJob extends Job {
	private List<Client> clients = new ArrayList<>();
	private Job job;
	
	BroadcastJob(Job job) {
		this.job = job;
	}
	
	void addClient(Client client) {
		clients.add(client);
	}
	
	@Override
	public Job execute(FileManager filemanager) {
		for (Client client : clients) {
			client.getConnection().write(job.getJson());
		}
		return null;
	}
	
	@Override
	public String getJson() {
		return null;
	}

}
