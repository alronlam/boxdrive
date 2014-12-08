package job;

import java.net.Socket;
import java.util.List;

import org.vertx.java.core.json.JsonObject;

import client.AbstractClient;

public class ListJob extends Job {
	
	ListJob(JsonObject json, AbstractClient client) {
		super(client);
	}

	private List<FileBean> files;
	
	@Override
	public void execute() {
		
	}

	@Override
	public String getJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
