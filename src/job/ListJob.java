package job;

import java.util.List;

import org.vertx.java.core.json.JsonObject;

import conn.Connection;

public class ListJob extends Job {
	
	ListJob(JsonObject json, Connection connection) {
		super(connection);
	}

	private List<FileBean> files;
	
	@Override
	public String executeLocal(JobManager jobManager) {
		return null;
	}

	@Override
	public String getJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
