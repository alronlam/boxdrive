package job;

import org.vertx.java.core.json.JsonObject;

import commons.Constants;

import file.FileBean;

public abstract class BasicJob extends Job {
	public FileBean file;
	
	
	/**
	 * Clone constructor.
	 * @param job
	 */
	BasicJob(BasicJob job) {
		super();
		this.file = new FileBean(job.file);
	}
	
	BasicJob(FileBean file) {

		this.file = file;
	}
	

	BasicJob(JsonObject json) {
		super();
		JsonObject body = json.getObject(Constants.JSON.BODY);
		file = new FileBean(body);
	}
}
