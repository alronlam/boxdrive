package client;

import job.Job;
import job.JobFactory;
import client.Connection;


public class Client {
	private Connection connection;
		
	public Connection getConnection() {
		return connection;
	}
	
	void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public int hashCode() {
		return this.connection.getSocket().hashCode();
	}
	
	
	
	protected void sendConfiguration() {
		// Send configuration to server
		Job configJob = JobFactory.getConfig(this);
		getConnection().write(configJob.getJson());				
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
}
