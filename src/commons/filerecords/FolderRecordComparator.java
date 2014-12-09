package commons.filerecords;

import java.net.Socket;
import java.util.ArrayList;

import job.Job;

public class FolderRecordComparator {
	/***
	 * This object is considered as the latest record, while the otherFolder is
	 * considered as the older record.
	 * 
	 * @param otherFolder
	 * @return
	 */
	public ArrayList<Job> compareAndGenerateJobs(FolderRecord oldRecord, FolderRecord newRecord, Socket serverSocket) {
		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.addAll(this.generateCreateJobs(oldRecord, newRecord, serverSocket));
		jobs.addAll(this.generateModifyJobs(oldRecord, newRecord, serverSocket));
		jobs.addAll(this.generateDeleteJobs(oldRecord, newRecord, serverSocket));
		return jobs;
	}

	private ArrayList<Job> generateCreateJobs(FolderRecord oldRecord, FolderRecord newRecord, Socket serverSocket) {
		ArrayList<Job> createJobs = new ArrayList<Job>();

		ArrayList<FileRecord> newFileRecords = newRecord.getList();
		ArrayList<FileRecord> oldFileRecords = oldRecord.getList();

		for (FileRecord newFileRecord : newFileRecords) {
			if (!oldFileRecords.contains(newFileRecord)) {

				// createJobs.add(new Job());
			}
		}

		return createJobs;
	}

	private ArrayList<Job> generateModifyJobs(FolderRecord oldRecord, FolderRecord newRecord, Socket serverSocket) {
		ArrayList<Job> modifyJobs = new ArrayList<Job>();

		return modifyJobs;
	}

	private ArrayList<Job> generateDeleteJobs(FolderRecord oldRecord, FolderRecord newRecord, Socket serverSocket) {
		ArrayList<Job> deleteJobs = new ArrayList<Job>();

		return deleteJobs;
	}
}
