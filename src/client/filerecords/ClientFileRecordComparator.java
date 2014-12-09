package client.filerecords;

import java.nio.file.Paths;
import java.util.ArrayList;

import job.CreateJob;
import job.DeleteJob;
import job.Job;
import conn.Connection;

public class ClientFileRecordComparator {

	public ArrayList<Job> compareAndGenerateJobs(ClientFileRecordManager oldRecord, ClientFileRecordManager newRecord,
			Connection serverConnection, String sharedFolderName) {
		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.addAll(this.generateCreateJobs(oldRecord, newRecord, serverConnection, sharedFolderName));
		jobs.addAll(this.generateModifyJobs(oldRecord, newRecord, serverConnection, sharedFolderName));
		jobs.addAll(this.generateDeleteJobs(oldRecord, newRecord, serverConnection, sharedFolderName));
		return jobs;
	}

	private ArrayList<Job> generateCreateJobs(ClientFileRecordManager oldRecord, ClientFileRecordManager newRecord,
			Connection serverConnection, String sharedFolderName) {
		ArrayList<Job> createJobs = new ArrayList<Job>();

		ArrayList<FileRecord> newFileRecords = newRecord.getList();
		ArrayList<FileRecord> oldFileRecords = oldRecord.getList();

		for (FileRecord newFileRecord : newFileRecords) {
			if (!oldFileRecords.contains(newFileRecord)) {
				// placed dummy because i currently have no access to the
				// calling app's shared folder
				createJobs.add(new CreateJob(Paths.get(sharedFolderName + "/" + newFileRecord.getFileName()),
						serverConnection));
			}
		}

		return createJobs;
	}

	private ArrayList<Job> generateModifyJobs(ClientFileRecordManager oldRecord, ClientFileRecordManager newRecord,
			Connection serverConnection, String sharedFolderName) {
		ArrayList<Job> modifyJobs = new ArrayList<Job>();

		ArrayList<FileRecord> newFileRecords = newRecord.getList();
		ArrayList<FileRecord> oldFileRecords = oldRecord.getList();

		for (FileRecord newFileRecord : newFileRecords) {
			int matchIndex = oldFileRecords.indexOf(newFileRecord);

			if (matchIndex >= 0) {
				FileRecord matchInOldRecords = oldFileRecords.get(matchIndex);
				if (newFileRecord.getDateTimeModified() > matchInOldRecords.getDateTimeModified()) {
					// placed dummy because i currently have no access to the
					// calling app's shared folder
					modifyJobs.add(new CreateJob(Paths.get(sharedFolderName + "/" + newFileRecord.getFileName()),
							serverConnection));
				}

			}
		}

		return modifyJobs;
	}

	private ArrayList<Job> generateDeleteJobs(ClientFileRecordManager oldRecord, ClientFileRecordManager newRecord,
			Connection serverConnection, String sharedFolderName) {
		ArrayList<Job> deleteJobs = new ArrayList<Job>();

		ArrayList<FileRecord> newFileRecords = newRecord.getList();
		ArrayList<FileRecord> oldFileRecords = oldRecord.getList();

		for (FileRecord oldFileRecord : oldFileRecords) {
			int matchIndex = newFileRecords.indexOf(oldFileRecord);

			if (matchIndex < 0) {
				// placed dummy because i currently have no access to the
				// calling app's shared folder
				deleteJobs.add(new DeleteJob(Paths.get(sharedFolderName + "/" + oldFileRecord.getFileName()), oldRecord
						.getTimeLastModified(), serverConnection));
			}
		}

		return deleteJobs;
	}
}
