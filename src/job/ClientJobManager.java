package job;

import client.filerecords.SyncManager;
import conn.Connection;

public class ClientJobManager extends JobManager {

	public void syncWithCoordinator(Connection serverConnection) {
		SyncManager.getInstance().clearFileRecords();

		Job listJob = new ListJob(serverConnection);
		this.handleNewJob(listJob);

		// wait in this monitor until the result has returned
		SyncManager.getInstance().waitForFileRecords();
	}
}
