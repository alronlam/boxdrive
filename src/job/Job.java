package job;

import java.net.Socket;

public abstract class Job implements Comparable<Job> {
	private Socket socket;
	private final long createTime = System.currentTimeMillis();
	private boolean toSend = true;
	
	Job(Socket socket) {
		this.socket = socket;
	}
	
	public abstract void execute();

	public long getCreateTime() {
		return createTime;
	}
	
	public void setForReceiving() {
		toSend = false;
	}
	
	public boolean isToSend() {
		return toSend;
	}
	
	protected Socket getSocket() {
		return socket;
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
