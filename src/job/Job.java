package job;

import conn.Connection;
import filemanager.FileManager;

public abstract class Job implements Comparable<Job> {
	protected Connection connection;
	private final long createTime = System.currentTimeMillis();
	private boolean toSend = true;
	
	Job(Connection connection) {
		this.connection = connection;
	}
	
	
	
	public abstract Job execute(FileManager filemanager);
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
	
	public Connection getConnection() {
		return connection;
	}
	
	public int compareTo(Job other) {
		int comparison = 0;
		if (createTime > other.createTime) {
			comparison = 1;
		} else if (createTime < other.createTime) {
			comparison = -1;
		}
		return comparison;
	}
}
