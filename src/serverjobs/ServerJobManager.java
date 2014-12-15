package serverjobs;

import job.BroadcastJob;
import job.Job;
import job.manager.JobManager;
import client.Client;
import file.FileManager;

public class ServerJobManager extends JobManager{

	public ServerJobManager(FileManager fileManager) {
		super(fileManager);
	}

	@Override
	protected void actuallyProcessMessages(Job job, Client client) {
		if (job.isToSend()) {
			client.getConnection().write(job.getJson());
		} else {
			Job toSend = job.execute(fileManager);
			if (toSend != null && !(toSend instanceof BroadcastJob)) {
				this.handleNewJob(toSend, client);
			}
		}
	}
}
