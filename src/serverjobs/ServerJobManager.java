package serverjobs;

import job.Job;
import job.JobManager;
import conn.ConnectionManager;

public class ServerJobManager extends JobManager{

	@Override
	protected synchronized void processMessages() {
		while (jobQueue.size() > 0) {
			Job currJob = this.dequeue(0);
			String toBroadcast = currJob.execute(this);
			
			if(toBroadcast != null)
				ConnectionManager.getInstance().broadcast(toBroadcast);
		}
	}
}
