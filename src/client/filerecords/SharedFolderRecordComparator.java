package client.filerecords;

import java.nio.file.Paths;
import java.util.ArrayList;

import job.CreateJob;
import job.DeleteJob;
import job.Job;
import conn.Connection;

public class SharedFolderRecordComparator {

	public ArrayList<Job> compareAndGenerateJobs(ArrayList<FileRecord> newFileRecords,
			ArrayList<FileRecord> oldFileRecords, long lastTimeOldRecordsModified, Connection serverConnection,
			String sharedFolderName) {
		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.addAll(this.generateCreateJobs(oldFileRecords, newFileRecords, serverConnection, sharedFolderName));
		jobs.addAll(this.generateModifyJobs(oldFileRecords, newFileRecords, serverConnection, sharedFolderName));
		jobs.addAll(this.generateDeleteJobs(oldFileRecords, newFileRecords, serverConnection, sharedFolderName,
				lastTimeOldRecordsModified));
		return jobs;
	}

	private ArrayList<Job> generateCreateJobs(ArrayList<FileRecord> newFileRecords,
			ArrayList<FileRecord> oldFileRecords, Connection serverConnection, String sharedFolderName) {
		ArrayList<Job> createJobs = new ArrayList<Job>();

		for (FileRecord newFileRecord : newFileRecords) {
			if (!oldFileRecords.contains(newFileRecord)) {
				createJobs.add(new CreateJob(Paths.get(sharedFolderName + "/" + newFileRecord.getFileName()),
						serverConnection));
			}
		}

		return createJobs;
	}

	private ArrayList<Job> generateModifyJobs(ArrayList<FileRecord> newFileRecords,
			ArrayList<FileRecord> oldFileRecords, Connection serverConnection, String sharedFolderName) {
		ArrayList<Job> modifyJobs = new ArrayList<Job>();

		for (FileRecord newFileRecord : newFileRecords) {
			int matchIndex = oldFileRecords.indexOf(newFileRecord);

			if (matchIndex >= 0) {
				FileRecord matchInOldRecords = oldFileRecords.get(matchIndex);
				if (newFileRecord.getDateTimeModified() > matchInOldRecords.getDateTimeModified()) {
					modifyJobs.add(new CreateJob(Paths.get(sharedFolderName + "/" + newFileRecord.getFileName()),
							serverConnection));
				}

			}
		}

		return modifyJobs;
	}

	private ArrayList<Job> generateDeleteJobs(ArrayList<FileRecord> newFileRecords,
			ArrayList<FileRecord> oldFileRecords, Connection serverConnection, String sharedFolderName,
			long lastTimeOldRecordsModified) {
		ArrayList<Job> deleteJobs = new ArrayList<Job>();

		for (FileRecord oldFileRecord : oldFileRecords) {
			int matchIndex = newFileRecords.indexOf(oldFileRecord);

			if (matchIndex < 0) {
				deleteJobs.add(new DeleteJob(Paths.get(sharedFolderName + "/" + oldFileRecord.getFileName()),
						lastTimeOldRecordsModified, serverConnection));
			}
		}

		return deleteJobs;
	}
}
