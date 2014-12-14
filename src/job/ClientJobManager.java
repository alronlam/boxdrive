package job;

import java.util.ArrayList;

import client.filerecords.ClientFileRecordManager;
import client.filerecords.FileRecord;
import client.filerecords.SharedFolderRecordComparator;
import client.filerecords.SyncManager;
import conn.Connection;

public class ClientJobManager extends JobManager {

	public void syncWithCoordinator(Connection serverConnection, ClientFileRecordManager fileRecordManager,
			String sharedFolderName) {
		SyncManager.getInstance().clearFileRecords();

		Job listJob = new ListJob(serverConnection);
		this.handleNewJob(listJob);

		// wait in this monitor until the result has returned
		SyncManager.getInstance().waitForFileRecords();

		// Compare with last record and generate appropriate jobs
		SharedFolderRecordComparator comparator = new SharedFolderRecordComparator();
		ArrayList<FileRecord> newFileRecords = SyncManager.getInstance().getFileRecords();
		ArrayList<FileRecord> oldFileRecords = fileRecordManager.getList();
		long lastTimeOldRecordsModified = fileRecordManager.getTimeLastModified();

		ArrayList<Job> newJobs = comparator.compareAndGenerateJobs(newFileRecords, oldFileRecords,
				lastTimeOldRecordsModified, serverConnection, sharedFolderName);

		System.out.println("\nSYNC RESULTS:");
		for (Job newJob : newJobs) {
			System.out.println(newJob.getJson());
			this.handleNewJob(newJob);
		}

		System.out.println("SYNC DONE:\n");
	}
}
