package job;

import java.util.ArrayList;
import java.util.Collections;

import org.vertx.java.core.json.JsonObject;

import conn.Connection;
import conn.ConnectionManager;

public class JobManager {

	protected ArrayList<Job> jobQueue;
	protected ConnectionManager connMgrClients,connMgrStorageServers;

	public JobManager() {
		this.jobQueue = new ArrayList<Job>();
	}

	public JobManager(ConnectionManager connMgr) {
		this.jobQueue = new ArrayList<Job>();
		this.connMgrClients = connMgr;
	}
	
	public JobManager(ConnectionManager connMgrClient, ConnectionManager connMgrStorageServer) {
		this.jobQueue = new ArrayList<Job>();
		this.connMgrClients = connMgrClient;
		this.connMgrStorageServers = connMgrStorageServer;
	}
	
	/**
	 * This method will be called when a new JSON message is received from a
	 * socket.
	 * 
	 * @param jsonString
	 * @param jsonJobHandlingThread
	 */
	public synchronized void handleNewJsonMessage(String jsonString, Connection connection) {
		JsonObject json = new JsonObject(jsonString);

		Job newJob = JobFactory.createJob(json, connection);
		handleNewJob(newJob);
	}

	public synchronized void handleNewJob(Job newJob) {
		this.enqueue(newJob);

		System.out.println("handleNewJob called.");
		if (jobQueue.size() == 1)
			this.processMessages();
	}

	private synchronized void enqueue(Job job) {
		this.jobQueue.add(job);
		Collections.sort(jobQueue);
	}

	protected synchronized Job dequeue(int index) {
		if (jobQueue.size() == 0)
			return null;
		return jobQueue.remove(0);
	}

	protected synchronized void processMessages() {
		while (jobQueue.size() > 0) {
			Job currJob = this.dequeue(0);
			currJob.execute(this);
		}
	}
}
