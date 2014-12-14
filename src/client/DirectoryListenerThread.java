package client;

import java.io.IOException;
import java.nio.file.Path;

import job.manager.JobManager;
import conn.Connection;

public class DirectoryListenerThread implements Runnable {

	private Path directoryPath;
	private Connection connection;
	private JobManager jobManager;
	
	public DirectoryListenerThread(Path directoryPath, Connection connection, JobManager jobManager) {
		this.directoryPath = directoryPath;
		this.connection = connection;
		this.jobManager = jobManager;
	}

	public void run() {
		try {
			new DirectoryListener(directoryPath, connection, jobManager).processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
