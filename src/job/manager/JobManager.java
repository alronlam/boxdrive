package job.manager;

import java.util.ArrayList;
import java.util.Collections;

import job.Job;
import job.JobFactory;

import org.vertx.java.core.json.JsonObject;

import conn.Connection;
import conn.ConnectionManager;
import filemanager.FileManager;

public abstract class JobManager {
	
	protected ArrayList<Job> jobQueue;
	protected FileManager fileManager;
	protected ConnectionManager connMgrClients, connMgrStorageServers;

	public JobManager() {
		this.jobQueue = new ArrayList<Job>();
	}

	public JobManager(ConnectionManager connMgr, FileManager fileManager) {
		this.jobQueue = new ArrayList<Job>();
		this.connMgrClients = connMgr;
		this.fileManager = fileManager;
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
	public synchronized void handleNewJsonMessage(String jsonString) {
		JsonObject json = new JsonObject(jsonString);

		Job newJob = JobFactory.createJob(json);
		handleNewJob(newJob);
	}

	public synchronized void handleNewJob(Job newJob) {
		this.enqueue(newJob);

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
			actuallyProcessMessages();			
		}
	}
	
	protected abstract void actuallyProcessMessages();
}
