package job.manager;

import java.util.ArrayList;
import java.util.Collections;

import job.Job;
import job.JobFactory;

import org.vertx.java.core.json.JsonObject;

import client.Client;
import file.FileManager;

public abstract class JobManager {
	
	protected ArrayList<JobClient> jobQueue;
	protected FileManager fileManager;
	

	public JobManager(FileManager fileManager) {
		this.jobQueue = new ArrayList<JobClient>();
		this.fileManager = fileManager;
	}
	
	/**
	 * This method will be called when a new JSON message is received from a
	 * socket.
	 * 
	 * @param jsonString
	 * @param jsonJobHandlingThread
	 */
	public synchronized void handleNewJsonMessage(String jsonString, Client client) {
		JsonObject json = new JsonObject(jsonString);

		Job newJob = JobFactory.createJob(json);
		handleNewJob(newJob, client);
	}

	public synchronized void handleNewJob(Job newJob, Client client) {
		this.enqueue(newJob, client);

		if (jobQueue.size() == 1)
			this.processMessages();
	}

	private synchronized void enqueue(Job job, Client client) {
		JobClient jobClient = new JobClient(job, client);
		this.jobQueue.add(jobClient);
		Collections.sort(jobQueue);
	}

	protected synchronized JobClient dequeue(int index) {
		if (jobQueue.size() == 0)
			return null;
		return jobQueue.remove(0);
	}

	protected synchronized void processMessages() {
		while (jobQueue.size() > 0) {
			actuallyProcessMessages();			
		}
	}
	
	protected class JobClient implements Comparable<JobClient> {
		Job job;
		Client client;
		JobClient(Job job, Client client) {
			this.job = job;
			this.client = client;
		}
		@Override
		public int compareTo(JobClient other) {
			Job otherJob = other.job;
			return this.job.compareTo(otherJob);
		}
	}
	
	protected abstract void actuallyProcessMessages();
}
