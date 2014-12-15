package server;

import java.util.ArrayList;
import java.util.List;

import job.BroadcastJob;
import job.manager.JobManager;
import client.Client;
import client.Connection;

public class ClientManager implements Connection.Listener {
	private List<Client> clients = new ArrayList<>();
	
	
	void add(Client client) {
		client.getConnection().setListener(this);
		clients.add(client);
	}
	
	/**
	 * Broadcasts to all clients immediately.
	 * @param job
	 * @param caller  Client to which the job would not be broadcasted to.
	 */
	public void broadcast(BroadcastJob job, Client caller, JobManager jobManager) {
		for (Client client : clients) {
			if (!client.equals(caller)) {
				jobManager.handleNewJob(job.getJob(), client);
			}
		}
	}
	
	@Override
	public void connectionDropped(Client client) {
		clients.remove(client);
		client.getConnection().cancel();
	}
}
