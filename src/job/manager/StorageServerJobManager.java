package job.manager;

import client.Client;
import file.FileManager;
import job.BroadcastJob;
import job.ConfigJob;
import job.Job;

public class StorageServerJobManager extends JobManager {
	
	public StorageServerJobManager(FileManager fileManager) {
		super(fileManager);
	}
	
	@Override
	protected void actuallyProcessMessages(Job job, Client client) {
		if (job.isToSend()) {
			client.getConnection().write(job.getJson());
		} else {
			if (job instanceof ConfigJob) {
				
				return;
			}
			
			Job toSend = job.execute(fileManager);
			if (toSend != null && !(toSend instanceof BroadcastJob)) {
				this.handleNewJob(toSend, client);
			}
		}
	}
}
