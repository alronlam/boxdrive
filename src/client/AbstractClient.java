package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import conn.ConnectionManager;
import job.Job;
import job.JobManager;

public abstract class AbstractClient {
	private JobManager jobManager;
	private ConnectionManager connectionManager;

	
	public AbstractClient() {
		jobManager = new JobManager();
		connectionManager = new ConnectionManager();
	}
	

	public Socket getSocket(int index) {
		return connectionManager.getSocket(index);
	}

	public Socket getFirstSocket() {
		return getSocket(0);
	}
	
	
	public JobManager getJobManager() {
		return jobManager;
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	@Override
	public int hashCode() {
		return getFirstSocket().hashCode();
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractClient))
			return false;
		
		AbstractClient other = (AbstractClient) obj;
		// TODO: Verify if identicality is sufficient
		if (this == other) {
			return true;
		} else {
			return false;
		}
	}
}
