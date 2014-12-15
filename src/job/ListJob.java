package job;

import java.util.List;

import org.vertx.java.core.json.JsonObject;

import client.Connection;
import file.FileBean;
import file.FileManager;

public class ListJob extends Job {
	
	ListJob(JsonObject json) {
		super();
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
