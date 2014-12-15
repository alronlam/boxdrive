package job.manager;

import conn.Client;
import filemanager.FileManager;
import job.BroadcastJob;
import job.Job;

public class ClientJobManager extends JobManager {
	Client client;
	
	public ClientJobManager(Client client, FileManager fileManager) {
		super(fileManager);
		this.client = client;
	}
	
	@Override
	protected void actuallyProcessMessages() {
		Job currJob = this.dequeue(0);
		if (currJob.isToSend()) {
			client.getConnection().write(currJob.getJson());
		} else {
			Job toSend = currJob.execute(fileManager);
			if (toSend != null && !(toSend instanceof BroadcastJob)) {
				this.handleNewJob(toSend);
			}
		}
	}
}
