package serverjobs;

import java.nio.file.Files;
import java.nio.file.Path;

import job.BasicJob;
import job.FileJob;
import job.Job;
import job.JobManager;
import job.RequestJob;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import conn.Connection;

public class ServerRequestJob extends RequestJob {

	/**
	 * Creates a Request with the same parameters as the job argument.
	 * 
	 * @param job
	 */
	public ServerRequestJob(BasicJob job) {
		super(job);
	}

	public ServerRequestJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	@Override
	public String executeLocal(JobManager jobManager) {
		Path localFile = file.getLocalizedFile();

		if (!Files.exists(localFile)) {
			return null;
		}

		Job forSending = new FileJob(localFile, this.getConnection());
		jobManager.handleNewJob(forSending);

		return null;
	}
}
