package file.filerecords;

import java.util.ArrayList;
import java.util.List;

import job.CreateJob;
import job.DeleteJob;
import job.Job;
import file.FileBean;

public class SharedFolderRecordComparator {

	public List<Job> compareAndGenerateJobs(List<FileBean> newFileRecords,
			List<FileBean> currFileRecords, List<FileBean> oldFileRecords,
			long lastTimeOldRecordsModified, String sharedFolderName) {

		System.out.println("\n Comparator: old = " + oldFileRecords.toString() + " curr = " + currFileRecords
				+ " new = " + newFileRecords.toString());

		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.addAll(this.generateCreateJobs(newFileRecords, currFileRecords, oldFileRecords,
				sharedFolderName));
		jobs.addAll(this.generateModifyJobs(newFileRecords, currFileRecords, sharedFolderName));
		jobs.addAll(this.generateDeleteJobs(newFileRecords, currFileRecords, oldFileRecords,
				sharedFolderName, lastTimeOldRecordsModified));
		return jobs;
	}

	private List<Job> generateCreateJobs(List<FileBean> serverFileRecords,
			List<FileBean> currFileRecords, List<FileBean> oldFileRecords, String sharedFolderName) {
		ArrayList<Job> createJobs = new ArrayList<Job>();

		// case where you created the file locally. so it should be in curr, but
		// not in old and not in server
		for (FileBean currFileRecord : currFileRecords) {
			if (!oldFileRecords.contains(currFileRecord) && !serverFileRecords.contains(currFileRecord)) {
				createJobs.add(new CreateJob(currFileRecord));
			}
		}

		// case where server created the file. so it should be in server, but
		// not in old and not in curr. create a job that acts as if the server
		// sent a create job
		for (FileBean serverFileRecord : serverFileRecords) {
			if (!oldFileRecords.contains(serverFileRecord) && !currFileRecords.contains(serverFileRecord)) {
				CreateJob createJob = new CreateJob(serverFileRecord);
				createJob.setForReceiving();
				createJobs.add(createJob);
			}
		}

		return createJobs;
	}

	private List<Job> generateModifyJobs(List<FileBean> newFileRecords,
			List<FileBean> currFileRecords, String sharedFolderName) {
		ArrayList<Job> modifyJobs = new ArrayList<Job>();

		for (FileBean newFileRecord : newFileRecords) {
			int matchIndex = currFileRecords.indexOf(newFileRecord);

			if (matchIndex >= 0) {
				FileBean matchInOldRecords = currFileRecords.get(matchIndex);
				long timeComparison = newFileRecord.getLastModified() - matchInOldRecords.getLastModified();

				if (timeComparison > 0) { // file you have locally is outdated
					// should change this to RequestJob to be more efficient
					modifyJobs.add(new CreateJob(newFileRecord));
				} else if (timeComparison < 0) { // file you have locally is new
													// version
					modifyJobs.add(new CreateJob(newFileRecord));
				}

			}
		}

		return modifyJobs;
	}

	private ArrayList<Job> generateDeleteJobs(List<FileBean> serverFileRecords,
			List<FileBean> currFileRecords, List<FileBean> oldFileRecords, 
			String sharedFolderName, long lastTimeOldRecordsModified) {
		ArrayList<Job> deleteJobs = new ArrayList<Job>();

		// case where you deleted it locally. so it should be in old and in
		// server, but not in curr.
		for (FileBean oldFileRecord : oldFileRecords) {

			if (serverFileRecords.contains(oldFileRecord) && !currFileRecords.contains(oldFileRecord)) {
				deleteJobs.add(new DeleteJob(oldFileRecord, lastTimeOldRecordsModified));
			}
		}

		// case where server deleted it. so it should be in old and curr, but
		// not in server. so create a delete job that acts like a message from
		// server

		for (FileBean oldFileRecord : oldFileRecords) {
			if (!serverFileRecords.contains(oldFileRecord) && currFileRecords.contains(oldFileRecord)) {
				DeleteJob deleteJob = new DeleteJob((oldFileRecord), lastTimeOldRecordsModified);
				deleteJob.setForReceiving();
				deleteJobs.add(deleteJob);
			}
		}

		return deleteJobs;
	}
}
