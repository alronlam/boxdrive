package job;

import java.net.Socket;

import client.AbstractClient;

public abstract class Job implements Comparable<Job> {
	private AbstractClient client;
	private final long createTime = System.currentTimeMillis();
	private boolean toSend = true;
	
	Job(AbstractClient client) {
		this.client= client;
	}
	
	public abstract void execute();
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
	
	AbstractClient getClient() {
		return client;
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
