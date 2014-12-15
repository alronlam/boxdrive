package conn;

import conn.Connection;


public abstract class Client {
	private Connection connection;
		
	public Connection getConnection() {
		return connection;
	}
	
	protected void setConnection(Connection connection) {
		this.connection = connection;
	}
}
