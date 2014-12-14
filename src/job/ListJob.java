package job;

import java.util.List;

import org.vertx.java.core.json.JsonObject;

import conn.Connection;
import filemanager.FileBean;
import filemanager.FileManager;

public class ListJob extends Job {
	
	ListJob(JsonObject json, Connection connection) {
		super(connection);
	}

	private List<FileBean> files;
	
	@Override
	public Job execute(FileManager filemanager) {
		return null;
	}

	@Override
	public String getJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
