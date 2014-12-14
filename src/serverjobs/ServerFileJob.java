package serverjobs;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import job.CreateJob;
import job.FileJob;
import job.Job;
import job.JobManager;
import job.RequestJob;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Base64;

import client.filerecords.ClientFileRecordManager;
import commons.Constants;
import conn.Connection;

public class ServerFileJob extends ServerBasicJob {
	// unsure if the constructors still need this
	// they probably dont
	protected final int BUFFER_SIZE = 8096;
	protected String fileByteString;

	/**
	 * Constructor for receiving.
	 * 
	 * @param json
	 * @param connection
	 */
	public ServerFileJob(JsonObject json, Connection connection) {
		super(json, connection);
		JsonObject body = json.getObject(Constants.JSON.BODY);
		fileByteString = body.getString(Constants.Body.FILEBYTES);
	}

	/**
	 * Constructor for sending.
	 * 
	 * @param path
	 * @param connection
	 */
	public ServerFileJob(Path path, Connection connection) {
		super(path, connection);
		try {
			fileByteString = Base64.encodeBytes(Files.readAllBytes(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String executeLocal(CoordinatorJobManager jobManager) {
		Connection c = null;
		
		// c isn't provided in current implementation
		// see comment in serverrequestjob
		Job forSending = new RequestJob(file.getJsonObject(), c);
		jobManager.handleNewJob(forSending);
		
		return null;
	}
	
	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.FILE);
		JsonObject body = file.getJsonObject();
		body.putString(Constants.Body.FILEBYTES, fileByteString);
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}

}
