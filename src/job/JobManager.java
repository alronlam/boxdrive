package job;

import java.util.ArrayList;
import java.util.Collections;

import org.vertx.java.core.json.JsonObject;

import conn.Connection;

public class JobManager {

	private ArrayList<Job> jobQueue;

	public JobManager() {
		this.jobQueue = new ArrayList<Job>();
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

		if (jobQueue.size() == 1)
			this.processMessages();
	}

	private synchronized void enqueue(Job job) {
		this.jobQueue.add(job);
		Collections.sort(jobQueue);
	}

	private synchronized Job dequeue(int index) {
		if (jobQueue.size() == 0)
			return null;
		return jobQueue.remove(0);
	}

	private synchronized void processMessages() {
		while (jobQueue.size() > 0) {
			Job currJob = this.dequeue(0);
			currJob.execute(this);
		}
	}
}
