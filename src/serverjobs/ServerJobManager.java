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
