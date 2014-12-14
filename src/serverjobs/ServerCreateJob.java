package serverjobs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import job.CreateJob;
import job.Job;
import job.JobManager;
import job.RequestJob;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import conn.Connection;

public class ServerCreateJob extends ServerBasicJob {

	public ServerCreateJob(Path path, Connection connection) {
		super(path, connection);
	}

	public ServerCreateJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	public String executeLocal(CoordinatorJobManager jobManager) {
		return null;
		
//		Path localFile = file.getLocalizedFile();
//
//		// Create folders immediately.
//		if (file.isDirectory()) {
//			try {
//				Files.createDirectory(localFile);
//			} catch (IOException ex) {
//				// FileAlreadyExistsException
//			}
//			return null;
//		}
//
//		if (Files.exists(localFile)) {
//			int comparison = file.compareLastModifiedTime(localFile);
//
//			// Send a new Create Job if local file is newer.
//			if (comparison > 0) {
//				Job forSending = new CreateJob(localFile, this.getConnection());
//				jobManager.handleNewJob(forSending);
//
//				// Update local file's last modified time if it is older and has
//				// same contents.
//			} else if (comparison < 0)
//				if (file.hasSameContents(localFile)) {
//					try {
//						Files.setLastModifiedTime(localFile, FileTime.fromMillis(file.getLastModified()));
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//
//					return this.getJson();
//					// Request for file if local file is older and has different
//					// contents.
//				} else {
//					Job forSending = new RequestJob(this);
//					jobManager.handleNewJob(forSending);
//				}
//			else{
//				//if comparison == 0 then just ignore the job
//			}
//		} else {
//
//			// File doesn't exist yet
//			Job forSending = new RequestJob(this);
//			jobManager.handleNewJob(forSending);
//		}
//		
//		return null;
	}
	
	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.CREATE);
		JsonObject body = file.getJsonObject();
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}

}