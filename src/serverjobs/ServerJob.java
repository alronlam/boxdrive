package serverjobs;

import conn.Connection;

public abstract class ServerJob implements Comparable<ServerJob> {
	private Connection connection;
	private final long createTime = System.currentTimeMillis();
	private boolean toSend = true;
	
	ServerJob(Connection connection) {
		this.connection = connection;
	}
	
	
	public String execute(ServerJobManager jobManager) {
		if (toSend) {
			connection.write(getJson());
			return null;
		} else {
			return executeLocal(jobManager);
		}
	}
	
	public abstract String executeLocal(ServerJobManager jobManager);
	public abstract String getJson();
	
	public long getCreateTime() {
		return createTime;
	}
	
	public void setForReceiving() {
		toSend = false;
	}
	
	public boolean isToSend() {
		return toSend;
	}
	
	protected Connection getConnection() {
		return connection;
	}
	
	public int compareTo(ServerJob other) {
		int comparison = 0;
		if (createTime > other.createTime) {
			comparison = 1;
		} else if (createTime < other.createTime) {
			comparison = -1;
		}
		return comparison;
	}
}
