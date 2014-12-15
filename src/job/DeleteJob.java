package job;

import org.vertx.java.core.json.JsonObject;

import client.Connection;
import commons.Constants;
import file.FileBean;
import file.FileManager;

public class DeleteJob extends BasicJob {

	DeleteJob(JsonObject json) {
		super(json);
	}

	/**
	 * @param path
	 *            A localized Path.
	 * @param lastModified
	 *            The time of deletion.
	 * @param socket
	 */
	public DeleteJob(FileBean file, long lastModified) {
		super(file);
		file.setLastModified(lastModified);
	}

	@Override
	public Job execute(FileManager filemanager) {
		if (!filemanager.exists(file)) {
			return null;
		}

		int comparison = filemanager.compareLastModifiedTime(file);

		// If local file is older, then safe to delete.
		if (comparison < 0) {
			filemanager.delete(file);
			return new BroadcastJob(this);
			
		} else {
			Job forSending = new CreateJob(this);
			return forSending;
		}
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
