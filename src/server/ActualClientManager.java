package server;

import java.util.ArrayList;
import java.util.List;

import job.BroadcastJob;
import client.Client;
import client.Connection;

public class ActualClientManager implements Connection.Listener {
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
	void broadcast(BroadcastJob job, Client caller) {
		for (Client client : clients) {
			if (!client.equals(caller)) {
				client.getConnection().write(job.getJson());
				// JobManager.handle(client, job); // In case you want to queue the jobs first.
			}
		}
	}
	
	@Override
	public void connectionDropped(Client client) {
		clients.remove(client);
		client.getConnection().cancel();
	}
}
