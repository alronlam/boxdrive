package client.filerecords;

import java.nio.file.Paths;
import java.util.ArrayList;

import job.CreateJob;
import job.DeleteJob;
import job.Job;
import conn.Connection;

public class SharedFolderRecordComparator {

	public ArrayList<Job> compareAndGenerateJobs(ArrayList<FileRecord> newFileRecords,
			ArrayList<FileRecord> currFileRecords, ArrayList<FileRecord> oldFileRecords,
			long lastTimeOldRecordsModified, Connection serverConnection, String sharedFolderName) {

		System.out.println("\n Comparator: old = " + oldFileRecords.toString() + " curr = " + currFileRecords
				+ " new = " + newFileRecords.toString());

		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.addAll(this.generateCreateJobs(newFileRecords, currFileRecords, oldFileRecords, serverConnection,
				sharedFolderName));
		jobs.addAll(this.generateModifyJobs(newFileRecords, currFileRecords, serverConnection, sharedFolderName));
		jobs.addAll(this.generateDeleteJobs(newFileRecords, currFileRecords, oldFileRecords, serverConnection,
				sharedFolderName, lastTimeOldRecordsModified));
		return jobs;
	}

	private ArrayList<Job> generateCreateJobs(ArrayList<FileRecord> serverFileRecords,
			ArrayList<FileRecord> currFileRecords, ArrayList<FileRecord> oldFileRecords, Connection serverConnection,
			String sharedFolderName) {
		ArrayList<Job> createJobs = new ArrayList<Job>();

		// case where you created the file locally. so it should be in curr, but
		// not in old and not in server
		for (FileRecord currFileRecord : currFileRecords) {
			if (!oldFileRecords.contains(currFileRecord) && !serverFileRecords.contains(currFileRecord)) {
				createJobs.add(new CreateJob(Paths.get(sharedFolderName + "/" + currFileRecord.getFileName()),
						serverConnection));
			}
		}

		// case where server created the file. so it should be in server, but
		// not in old and not in curr. create a job that acts as if the server
		// sent a create job
		for (FileRecord serverFileRecord : serverFileRecords) {
			if (!oldFileRecords.contains(serverFileRecord) && !currFileRecords.contains(serverFileRecord)) {
				CreateJob createJob = new CreateJob(Paths.get(sharedFolderName + "/" + serverFileRecord.getFileName()),
						serverConnection);
				createJob.setForReceiving();
				createJobs.add(createJob);
			}
		}

		return createJobs;
	}

	private ArrayList<Job> generateModifyJobs(ArrayList<FileRecord> newFileRecords,
			ArrayList<FileRecord> currFileRecords, Connection serverConnection, String sharedFolderName) {
		ArrayList<Job> modifyJobs = new ArrayList<Job>();

		for (FileRecord newFileRecord : newFileRecords) {
			int matchIndex = currFileRecords.indexOf(newFileRecord);

			if (matchIndex >= 0) {
				FileRecord matchInOldRecords = currFileRecords.get(matchIndex);
				long timeComparison = newFileRecord.getDateTimeModified() - matchInOldRecords.getDateTimeModified();

				if (timeComparison > 0) { // file you have locally is outdated
					// should change this to RequestJob to be more efficient
					modifyJobs.add(new CreateJob(Paths.get(sharedFolderName + "/" + newFileRecord.getFileName()),
							serverConnection));
				} else if (timeComparison < 0) { // file you have locally is new
													// version
					modifyJobs.add(new CreateJob(Paths.get(sharedFolderName + "/" + newFileRecord.getFileName()),
							serverConnection));
				}

			}
		}

		return modifyJobs;
	}

	private ArrayList<Job> generateDeleteJobs(ArrayList<FileRecord> serverFileRecords,
			ArrayList<FileRecord> currFileRecords, ArrayList<FileRecord> oldFileRecords, Connection serverConnection,
			String sharedFolderName, long lastTimeOldRecordsModified) {
		ArrayList<Job> deleteJobs = new ArrayList<Job>();

		// case where you deleted it locally. so it should be in old and in
		// server, but not in curr.
		for (FileRecord oldFileRecord : oldFileRecords) {

			if (serverFileRecords.contains(oldFileRecord) && !currFileRecords.contains(oldFileRecord)) {
				deleteJobs.add(new DeleteJob(Paths.get(sharedFolderName + "/" + oldFileRecord.getFileName()),
						lastTimeOldRecordsModified, serverConnection));
			}
		}

		// case where server deleted it. so it should be in old and curr, but
		// not in server. so create a delete job that acts like a message from
		// server

		for (FileRecord oldFileRecord : oldFileRecords) {
			if (!serverFileRecords.contains(oldFileRecord) && currFileRecords.contains(oldFileRecord)) {
				DeleteJob deleteJob = new DeleteJob(Paths.get(sharedFolderName + "/" + oldFileRecord.getFileName()),
						lastTimeOldRecordsModified, serverConnection);
				deleteJob.setForReceiving();
				deleteJobs.add(deleteJob);
			}
		}

		return deleteJobs;
	}
}
