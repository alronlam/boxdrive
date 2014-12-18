package client;

import job.manager.JobManager;
import client.Connection;

/**
 * Generic Client class. On its own, can be used as proxies for
 * extended Clients. A fully functioning Client requires both
 * a Connection and a JobManager.
 *
 */
public class Client {
	private Connection connection;
	private JobManager jobManager;
	
	/**
	 * Implicit constructor for subclasses.
	 */
	protected Client() {}
	
	public Client(Connection connection, JobManager jobManager) {
		this.connection = connection;
		connection.setClient(this);
		this.setJobManager(jobManager);
	}

	protected void setJobManager(JobManager jobManager) {
		this.jobManager = jobManager;
	}
	
	public JobManager getJobManager() {
		return jobManager;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	protected void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void listenForJobs() {
		new JSONJobHandlingThread().start();
	}
	
	@Override
	public int hashCode() {
		return this.connection.getSocket().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Client))
			return false;
		
		Client other = (Client) obj;
		if (this == other) {
			return true;
		} else {
			return false;
		}
	}

	private class JSONJobHandlingThread extends Thread {
		JSONJobHandlingThread(){
			this.setName("JSONThread for " + Client.this.getConnection().getIdentifier());
		}
		
		@Override
		public void run() {
			String json;
	
			while (true) {
				json = getConnection().read();
				jobManager.handleNewJsonMessage(json, Client.this);
			}
		}
	}
}
