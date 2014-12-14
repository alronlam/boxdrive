package job.manager;

import job.BroadcastJob;
import job.Job;

public class ClientJobManager extends JobManager {
	
	@Override
	protected void actuallyProcessMessages() {
		Job currJob = this.dequeue(0);
		if (currJob.isToSend()) {
			// Send shits
		} else {
			Job toSend = currJob.execute(fileManager);
			if (toSend != null && !(toSend instanceof BroadcastJob)) {
				this.handleNewJob(toSend);
			}
		}
	}
	
}
