package serverjobs;

import java.util.ArrayList;

import client.Connection;
import client.ConnectionManager;
import server_manager.FileDirectory;
import server_manager.FileObject;
import job.Job;
import job.manager.JobManager;

public class ServerJobManager extends JobManager{

	public ServerJobManager(ConnectionManager connMgr) {
		super(connMgr);
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
