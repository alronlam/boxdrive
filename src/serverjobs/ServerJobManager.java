package serverjobs;

import java.util.ArrayList;
import java.util.Collections;

import org.vertx.java.core.json.JsonObject;

import job.Job;
import job.JobFactory;
import job.JobManager;
import conn.Connection;
import conn.ConnectionManager;

public class ServerJobManager {

	protected ArrayList<ServerJob> jobQueue;

	public ServerJobManager() {
		this.jobQueue = new ArrayList<ServerJob>();
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

		ServerJob newJob = ServerJobFactory.createJob(json, connection);
		handleNewJob(newJob);
	}

	public synchronized void handleNewJob(ServerJob newJob) {
		this.enqueue(newJob);

		if (jobQueue.size() == 1)
			this.processMessages();
	}

	private synchronized void enqueue(ServerJob job) {
		this.jobQueue.add(job);
		Collections.sort(jobQueue);
	}

	protected synchronized ServerJob dequeue(int index) {
		if (jobQueue.size() == 0)
			return null;
		return jobQueue.remove(0);
	}

	protected synchronized void processMessages() {
		while (jobQueue.size() > 0) {
			ServerJob currJob = this.dequeue(0);
			String toBroadcast = currJob.execute(this);
			
			if(toBroadcast != null)
				ConnectionManager.getInstance().broadcast(toBroadcast);
		}
	}
}
