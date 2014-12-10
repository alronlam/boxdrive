package job;

import conn.Connection;

public abstract class Job implements Comparable<Job> {
	private Connection connection;
	private final long createTime = System.currentTimeMillis();
	private boolean toSend = true;
	
	Job(Connection connection) {
		this.connection = connection;
	}
	
	
	public void execute(JobManager jobManager) {
		if (toSend) {
			connection.write(getJson());
		} else {
			executeLocal(jobManager);
		}
	}
	
	public abstract void executeLocal(JobManager jobManager);
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
