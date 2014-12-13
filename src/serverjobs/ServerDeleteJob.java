package serverjobs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import job.CreateJob;
import job.DeleteJob;
import job.Job;
import job.JobManager;

import org.vertx.java.core.json.JsonObject;

import client.filerecords.ClientFileRecordManager;

import commons.Constants;

import conn.Connection;

public class ServerDeleteJob extends DeleteJob {

	public ServerDeleteJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	public ServerDeleteJob(Path path, long lastModified, Connection connection) {
		super(path, lastModified, connection);
	}

	@Override
	public String executeLocal(JobManager jobManager) {
		Path localFile = file.getLocalizedFile();
		if (!Files.exists(localFile)) {
			return null;
		}

		int comparison = file.compareLastModifiedTime(localFile);

		// If local file is older, then safe to delete.
		if (comparison < 0) {
			try {
				deleteRecursive(localFile.toFile());

				// Update the FolderRecord
				ClientFileRecordManager.getInstance().delete(file.getFilename(), file.getLastModified());
				return this.getJson();
			} catch (IOException ex) {
				System.err.println("Error deleting file.");
			}

			// If local file is newer, send a Create Job to remote.
		} else {
			Job forSending = new CreateJob(localFile, this.getConnection());
			jobManager.handleNewJob(forSending);
		}
		
		return null;
	}
}
