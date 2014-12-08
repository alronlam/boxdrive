package job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import org.vertx.java.core.json.JsonObject;

public class DeleteJob extends BasicJob {

	DeleteJob(JsonObject json, Socket socket) {
		super(json, socket);
	}
	
	/**
	 * @param path A localized Path.
	 * @param lastModified The time of deletion. 
	 * @param socket
	 */
	DeleteJob(Path path, long lastModified, Socket socket) {
		super(path, socket);
		file.setLastModified(lastModified);
	}

	
	@Override
	public void execute() {
		Path localFile = file.getLocalizedFile();
		if (Files.exists(localFile)) {
			return;
		}
		
		int comparison = file.compareLastModifiedTime(localFile);
		
		// If local file is older, then safe to delete.
		if (comparison < 0) {
			try {
				deleteRecursive(localFile.toFile());
			} catch (IOException ex) {
				System.err.println("Error deleting file.");
			}
		
		// If local file is newer, send a Create Job to remote.
		} else {
			Job forSending = new CreateJob(localFile, this.getSocket());
			JobManager.getInstance().handleNewJob(forSending);
		}
	}	
		
	
	/**
	 * {@link http://stackoverflow.com/a/4026761/2247074}
	 * @throws FileNotFoundException 
	 */
	private synchronized boolean deleteRecursive(File path) throws FileNotFoundException {
		System.out.println("Deleting: " + path.toString());
        
		if (!path.exists()) throw new FileNotFoundException(path.getAbsolutePath());
        boolean ret = true;
        if (path.isDirectory()) {
            for (File f : path.listFiles()){
                ret = ret && deleteRecursive(f);
            }
        }
        return ret && path.delete();
	}
}
