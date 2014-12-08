package job;

import org.vertx.java.core.json.JsonObject;

import util.Constants;

class FileBean {
	private String filename;
	private long lastModified;
	private byte[] checksum;
	private boolean isDirectory = false;
	
	/**
	 * @param body
	 */
	FileBean(JsonObject body) {
		filename = body.getString(Constants.Body.FILENAME);
		lastModified = body.getLong(Constants.Body.LAST_MODIFIED);
		checksum = body.getBinary(Constants.Body.CHECKSUM);
		isDirectory = body.getBoolean(Constants.Body.IS_DIRECTORY);
	}
	
	String getFilename() {
		return filename;
	}
	
	long getLastModified() {
		return lastModified;
	}
	
	byte[] getChecksum() {
		return checksum;
	}
	
	boolean isDirectory() {
		return isDirectory;
	}
}
