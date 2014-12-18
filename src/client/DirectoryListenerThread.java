package client;

import java.io.IOException;
import java.nio.file.Path;

import file.FileManager;
import job.manager.JobManager;

public class DirectoryListenerThread implements Runnable {

	private Path directoryPath;
	private Client client; 
	private JobManager jobManager;
	private FileManager fileManager;
	
	public DirectoryListenerThread(Path directoryPath, Client client, JobManager jobManager, FileManager fileManager) {
		this.directoryPath = directoryPath;
		this.client = client;
		this.jobManager = jobManager;
		this.fileManager = fileManager;
	}

	public void run() {
		try {
			new DirectoryListener(directoryPath, client, jobManager, fileManager).processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
