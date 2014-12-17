package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import job.ConfigJob;

public class ConfigManager {
	private String localFolder;
	private final String FILENAME = "config.ser";
	private LocalConfig config;
	private StorageServer server;
	
	public ConfigManager(StorageServer server, String localFolder) {
		this.server = server;
		this.localFolder = localFolder;
	}
	
	public void handleReceiveConfig(ConfigJob job) {
		config = new LocalConfig(job.getConfiguration(), job.getServerId());
		try {
	         FileOutputStream fileOut = new FileOutputStream(this.getLocalFilename());
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(config);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in " + this.getLocalFilename());
	      } catch(IOException ex) {
	          ex.printStackTrace();
	      }
	}
	
	public void sendConfig() {
		Path configFile = Paths.get(this.getLocalFilename());
		ConfigJob job;
		if (Files.exists(configFile)) {
			try {
				FileInputStream fileIn = new FileInputStream(this.getLocalFilename());
		        ObjectInputStream in = new ObjectInputStream(fileIn);
		        config = (LocalConfig) in.readObject();
		        in.close();
		        fileIn.close();
			} catch(IOException ex) {
		          ex.printStackTrace();
			} catch(ClassNotFoundException ex) {
		         ex.printStackTrace();
		    }
			job = ConfigJob.getStorageServer(config.getConfiguration(), config.getServerId());
		
		} else {
			job = ConfigJob.getStorageServer();
		}
		
		server.getConnection().write(job.getJson());
	}
	
	private String getLocalFilename() {
		return localFolder + "/" + FILENAME;
	}
}
