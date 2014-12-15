package client;

import java.io.IOException;
import java.nio.file.Path;

import job.manager.JobManager;
import conn.Client;
import conn.Connection;

public class DirectoryListenerThread implements Runnable {

	private Path directoryPath;
	private Client client; 
	private JobManager jobManager;
	
	public DirectoryListenerThread(Path directoryPath, Client client, JobManager jobManager) {
		this.directoryPath = directoryPath;
		this.client = client;
		this.jobManager = jobManager;
	}

	public void run() {
		try {
			new DirectoryListener(directoryPath, client, jobManager).processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
