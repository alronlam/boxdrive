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
	private final String FILENAME = "config.ser";
	private LocalConfig config;
	private StorageServer server;
	
	public ConfigManager(StorageServer server) {
		this.server = server;
	}
	
	public void handleReceiveConfig(ConfigJob job) {
		this.config = new LocalConfig(job.getConfiguration(), job.getServerId());
		try {
	         FileOutputStream fileOut = new FileOutputStream(FILENAME);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(config);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in " + FILENAME);
	      } catch(IOException ex) {
	          ex.printStackTrace();
	      }
	}
	
	public void sendConfig() {
		Path configFile = Paths.get(FILENAME);
		ConfigJob job;
		if (Files.exists(configFile)) {
			try {
				FileInputStream fileIn = new FileInputStream(FILENAME);
		        ObjectInputStream in = new ObjectInputStream(fileIn);
		        config = (LocalConfig) in.readObject();
		        in.close();
		        fileIn.close();
			} catch(IOException ex) {
		          ex.printStackTrace();
			} catch(ClassNotFoundException ex) {
		         ex.printStackTrace();
		    }
			job = ConfigJob.getStorageServer(config.configuration, config.serverId);
		
		} else {
			job = ConfigJob.getStorageServer();
		}
		
		server.getConnection().write(job.getJson());
	}
	
	private class LocalConfig implements Serializable {
		/**/
		private static final long serialVersionUID = 7302974398350922946L;
		private String configuration; 
		private int serverId;
		private LocalConfig(String configuration, int serverId) {
			this.configuration = configuration;
			this.serverId = serverId;
		}
	}
}
