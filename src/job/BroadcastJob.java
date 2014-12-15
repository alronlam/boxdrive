package job;

import java.util.ArrayList;
import java.util.List;

import client.Client;
import file.FileManager;

public class BroadcastJob extends Job {
	private Job job;
	
	BroadcastJob(Job job) {
		this.job = job;
		job.setForSending();
	}
	
	/**
	 * Does nothing. ClientManagers should handle the actual broadcast.
	 */
	@Override
	public Job execute(FileManager filemanager) {
		return null;
	}
	
	public Job getJob() {
		return job;
	}
	
	@Override
	public String getJson() {
		return job.getJson();
	}

}
