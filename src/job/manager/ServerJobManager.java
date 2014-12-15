package job.manager;

import server.ClientManager;
import client.Client;
import file.FileManager;
import job.BroadcastJob;
import job.Job;

public class ServerJobManager extends JobManager {
	private ClientManager clientManager;
	
	public ServerJobManager(FileManager fileManager, ClientManager clientManager) {
		super(fileManager);
		this.clientManager = clientManager;
	}
	
	@Override
	protected void actuallyProcessMessages(Job job, Client client) {
		if (job.isToSend()) {
			client.getConnection().write(job.getJson());
		} else {
			Job toSend = job.execute(fileManager);
			if (toSend != null) {
				if (toSend instanceof BroadcastJob) {
					handleBroadcast(toSend, client);
				}
				else {
					this.handleNewJob(toSend, client);
				}
			}
		}
	}
	
	
	private void handleBroadcast(Job job, Client caller) {
		
		BroadcastJob toBroadcast = (BroadcastJob) job;
		clientManager.broadcast(toBroadcast, caller, this);
	}
}
