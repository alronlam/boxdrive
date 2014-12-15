package client;

import client.Connection;


public abstract class Client {
	private Connection connection;
		
	public Connection getConnection() {
		return connection;
	}
	
	protected void setConnection(Connection connection) {
		this.connection = connection;
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
}
