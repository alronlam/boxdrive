package job.manager;

import client.Client;
import file.FileManager;
import job.BroadcastJob;
import job.Job;

public class ClientJobManager extends JobManager {
	
	public ClientJobManager(FileManager fileManager) {
		super(fileManager);
	}
	
	@Override
	protected void actuallyProcessMessages() {
		JobClient jc = this.dequeue(0);
		Job currJob = jc.job;
		Client client = jc.client;
		if (currJob.isToSend()) {
			client.getConnection().write(currJob.getJson());
		} else {
			Job toSend = currJob.execute(fileManager);
			if (toSend != null && !(toSend instanceof BroadcastJob)) {
				this.handleNewJob(toSend, client);
			}
		}
	}
}
