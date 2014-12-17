package server;

import java.io.Serializable;

public class LocalConfig implements Serializable {
	/**/
	private static final long serialVersionUID = 7302974398350922946L;
	private String configuration; 
	private int serverId;
	
	LocalConfig(String configuration, int serverId) {
		this.configuration = configuration;
		this.serverId = serverId;
	}
	
	String getConfiguration() {
		return configuration;
	}
	
	int getServerId() {
		return serverId;
	}
}