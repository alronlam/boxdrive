package job.manager;

import server.ConfigManager;
import client.Client;
import file.FileManager;
import job.BroadcastJob;
import job.ConfigJob;
import job.Job;

public class StorageServerJobManager extends JobManager {
	private ConfigManager configManager;
	
	public StorageServerJobManager(FileManager fileManager, ConfigManager configManager) {
		super(fileManager);
		this.configManager = configManager;
	}
	
	@Override
	protected void actuallyProcessMessages(Job job, Client client) {
		if (job.isToSend()) {
			client.getConnection().write(job.getJson());
		} else {
			if (job instanceof ConfigJob) {
				configManager.handleReceiveConfig((ConfigJob) job);
				return;
			}
			
			Job toSend = job.execute(fileManager);
			if (toSend != null && !(toSend instanceof BroadcastJob)) {
				this.handleNewJob(toSend, client);
			}
		}
	}
}
