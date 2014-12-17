package job;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;
import file.FileBean;
import file.FileManager;

public class CreateJob extends BasicJob {
	
	/**
	 * Clone constructor
	 * @param job
	 */
	public CreateJob(BasicJob job) {
		super(job);
	}
	
	/**
	 * Constructor for sending.
	 * 
	 * @param file
	 */
	public CreateJob(FileBean file) {
		super(file);
	}

	public CreateJob(JsonObject json) {
		super(json);
	}

	@Override
	public Job execute(FileManager filemanager) {
		// Create folders immediately.
		if (file.isDirectory()) {
			if (!filemanager.exists(file)) {
				filemanager.createDirectory(file);
				Job forSending = new BroadcastJob(this);
				return forSending;	
			}
			
		} else if (filemanager.exists(file)) {
			int comparison = filemanager.compareLastModifiedTime(file);

			// Send a new Create Job if local file is newer.
			if (comparison > 0) {
				FileBean updatedFile = filemanager.getFileBean(file.getFilename());
				Job forSending = new CreateJob(updatedFile);
				return forSending;
				
				// jobManager.handleNewJob(forSending);

				// Update local file's last modified time if it is older and has
				// same contents.
			} else if (comparison < 0)
				if (filemanager.hasSameContents(file)) {
					filemanager.setLastModifiedTime(file);

					Job forSending = new BroadcastJob(this);
					return forSending;
					// Request for file if local file is older and has different
					// contents.
				} else {
					Job forSending = new RequestJob(this);
					return forSending;
				}
			else{
				// if comparison == 0 then just ignore the job
			}
			
		// File doesn't exist yet		
		} else {
			Job forSending = new RequestJob(this);
			return forSending;
		}
		
		return null;
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
