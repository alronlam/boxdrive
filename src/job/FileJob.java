package job;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Base64;

import commons.Constants;

import conn.Connection;

public class FileJob extends BasicJob {
	private final int BUFFER_SIZE = 8096;
	private String fileByteString;

	/**
	 * Constructor for receiving.
	 * 
	 * @param json
	 * @param connection
	 */
	FileJob(JsonObject json, Connection connection) {
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
	public FileJob(Path path, Connection connection) {
		super(path, connection);
		try {
			fileByteString = Base64.encodeBytes(Files.readAllBytes(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String executeLocal(JobManager jobManager) {
		// TODO handle newer existing file
		Path localFile = file.getLocalizedFile();

		byte[] fileBytes = Base64.decode(fileByteString);
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
				FileOutputStream outputStream = new FileOutputStream(localFile.toFile())) {
			int read = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((read = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, read);
				System.out.println("wrting: " + buffer);
			}
			Files.setLastModifiedTime(localFile, FileTime.fromMillis(file.getLastModified()));

			// Connection is null because this job is just created to be able to
			// construct its JSON. It won't reall be processed.
			CreateJob createJobForBroadcasting = new CreateJob(localFile, null);

			return createJobForBroadcasting.getJson();

		} catch (IOException ex) {
			try {
				Files.delete(localFile);
				System.err.println("Error. Deleting: " + localFile.toString());
			} catch (IOException ex1) {
				// something went terribly, horribly wrong
				ex1.printStackTrace();
			}
		}
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
