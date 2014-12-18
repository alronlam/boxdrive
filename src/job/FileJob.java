package job;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import file.FileBean;
import file.FileManager;

public class FileJob extends BasicJob {
	private String fileByteString;

	/**
	 * Constructor for receiving.
	 * 
	 * @param json
	 * @param connection
	 */
	FileJob(JsonObject json) {
		super(json);
		JsonObject body = json.getObject(Constants.JSON.BODY);
		fileByteString = body.getString(Constants.Body.FILEBYTES);
	}

	public FileJob(FileBean file, String fileByteString) {
		super(file);
		this.fileByteString = fileByteString;
	}
	
	public FileJob(FileBean file, FileManager filemanager) {
		this(file, filemanager.getFileBytes(file)); 
	}

	@Override
	public Job execute(FileManager filemanager) {
		// TODO handle newer existing file
		boolean success = filemanager.createFile(file, fileByteString);
		if (success) {
			Job createJob = new CreateJob(this);
			return new BroadcastJob(createJob);
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
