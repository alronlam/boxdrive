package job;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import org.vertx.java.core.json.JsonObject;

public class CreateJob extends BasicJob {
	
	/**
	 * Constructor for sending.
	 * @param path A localized path.
	 * @param socket
	 */
	CreateJob(Path path, Socket socket) {
		super(path, socket);
	}
	
	CreateJob(JsonObject json, Socket socket) {
		super(json, socket);
	}

	@Override
	public void execute() {
		Path localFile = file.getLocalizedFile();
		
		// Create folders immediately.
		if (file.isDirectory()) {
			try {
				Files.createDirectory(localFile);
			} catch (IOException ex) {
				// FileAlreadyExistsException
			}
			return;
		} 
		
		if (Files.exists(localFile)) {
			int comparison = file.compareLastModifiedTime(localFile);
			
			// Send a new Create Job if local file is newer.  
			if (comparison > 0) {
				Job forSending = new CreateJob(localFile, this.getSocket());
				JobManager.getInstance().handleNewJob(forSending);
			
			// Update local file's last modified time if it is older and has same contents.
			} else if (comparison < 0 && file.hasSameContents(localFile)) {
				try {
					Files.setLastModifiedTime(localFile, FileTime.fromMillis(file.getLastModified()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			// Request for file if local file is older and has different contents.
			} else {
				Job forSending = new RequestJob(this);
				JobManager.getInstance().handleNewJob(forSending);
			}
			
		// File doesn't exist yet
		} else {
			Job forSending = new RequestJob(this);
			JobManager.getInstance().handleNewJob(forSending);
		}
	}	
}