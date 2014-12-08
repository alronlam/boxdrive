package commons.filerecords;

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
	public ArrayList<Job> compareAndGenerateJobs(FolderRecord oldRecord, FolderRecord newRecord) {
		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.addAll(this.generateCreateJobs(oldRecord, newRecord));
		jobs.addAll(this.generateModifyJobs(oldRecord, newRecord));
		jobs.addAll(this.generateDeleteJobs(oldRecord, newRecord));
		return jobs;
	}

	private ArrayList<Job> generateCreateJobs(FolderRecord oldRecord, FolderRecord newRecord) {
		ArrayList<Job> createJobs = new ArrayList<Job>();

		return createJobs;
	}

	private ArrayList<Job> generateModifyJobs(FolderRecord oldRecord, FolderRecord newRecord) {
		ArrayList<Job> modifyJobs = new ArrayList<Job>();

		return modifyJobs;
	}

	private ArrayList<Job> generateDeleteJobs(FolderRecord oldRecord, FolderRecord newRecord) {
		ArrayList<Job> deleteJobs = new ArrayList<Job>();

		return deleteJobs;
	}
}
