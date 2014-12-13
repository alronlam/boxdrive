package serverjobs;

import java.util.ArrayList;

import server_manager.FileDirectory;
import server_manager.FileObject;
import job.Job;
import job.JobManager;
import conn.Connection;
import conn.ConnectionManager;

public class ServerJobManager extends JobManager{
	public FileDirectory fileDirectory;

	public ServerJobManager(ConnectionManager connMgr) {
		super(connMgr);
	}
	
	public ServerJobManager(ConnectionManager connMgrClients,ConnectionManager connMgrStorageServers) {
		super(connMgrClients,connMgrStorageServers);
	}
	
	public void updateFileDirectory(){
		fileDirectory.addServer(connMgrStorageServers.getLastConnection());
	}
	
	public String forwardJob(ServerJob job){
		//fileDirectory.getServersFromConfig(config);
		return null;
	}

	@Override
	protected synchronized void processMessages() {
		while (jobQueue.size() > 0) {
			Job currJob = this.dequeue(0);
			
			String toBroadcast = currJob.execute(this);
			
			if(toBroadcast != null)
				connMgrClients.broadcast(toBroadcast);
		}
	}
}
