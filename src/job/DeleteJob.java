package job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

import client.filerecords.ClientFileRecordManager;

import commons.Constants;

import conn.Connection;

public class DeleteJob extends BasicJob {

	DeleteJob(JsonObject json, Connection connection) {
		super(json, connection);
	}

	/**
	 * @param path
	 *            A localized Path.
	 * @param lastModified
	 *            The time of deletion.
	 * @param socket
	 */
	public DeleteJob(Path path, long lastModified, Connection connection) {
		super(path, connection);
		file.setLastModified(lastModified);
	}

	@Override
	public void executeLocal(JobManager jobManager) {
		Path localFile = file.getLocalizedFile();
		if (!Files.exists(localFile)) {
			return;
		}

		int comparison = file.compareLastModifiedTime(localFile);

		// If local file is older, then safe to delete.
		if (comparison < 0) {
			try {
				deleteRecursive(localFile.toFile());

				// Update the FolderRecord
				ClientFileRecordManager.getInstance().delete(file.getFilename(), file.getLastModified());

			} catch (IOException ex) {
				System.err.println("Error deleting file.");
			}

			// If local file is newer, send a Create Job to remote.
		} else {
			Job forSending = new CreateJob(localFile, this.getConnection());
			jobManager.handleNewJob(forSending);
		}
	}

	/**
	 * {@link http://stackoverflow.com/a/4026761/2247074}
	 * 
	 * @throws FileNotFoundException
	 */
	private synchronized boolean deleteRecursive(File path) throws FileNotFoundException {
		System.out.println("Deleting: " + path.toString());

		if (!path.exists())
			throw new FileNotFoundException(path.getAbsolutePath());
		boolean ret = true;
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				ret = ret && deleteRecursive(f);
			}
		}
		return ret && path.delete();
	}

	@Override
	public String getJson() {
		JsonObject json = new JsonObject();
		json.putString(Constants.JSON.TYPE, Constants.Type.DELETE);
		JsonObject body = file.getJsonObject();
		json.putObject(Constants.JSON.BODY, body);
		return json.encode();
	}
}
