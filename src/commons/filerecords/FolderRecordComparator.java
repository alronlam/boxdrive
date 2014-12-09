package commons.filerecords;

import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;

import job.CreateJob;
import job.DeleteJob;
import job.Job;

public class FolderRecordComparator {

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
				// placed dummy because i currently have no access to the
				// calling app's shared folder
				createJobs.add(new CreateJob(Paths.get("dummy/" + newFileRecord.getFileName()), serverSocket));
			}
		}

		return createJobs;
	}

	private ArrayList<Job> generateModifyJobs(FolderRecord oldRecord, FolderRecord newRecord, Socket serverSocket) {
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
					modifyJobs.add(new CreateJob(Paths.get("dummy/" + newFileRecord.getFileName()), serverSocket));
				}

			}
		}

		return modifyJobs;
	}

	private ArrayList<Job> generateDeleteJobs(FolderRecord oldRecord, FolderRecord newRecord, Socket serverSocket) {
		ArrayList<Job> deleteJobs = new ArrayList<Job>();

		ArrayList<FileRecord> newFileRecords = newRecord.getList();
		ArrayList<FileRecord> oldFileRecords = oldRecord.getList();

		for (FileRecord oldFileRecord : oldFileRecords) {
			int matchIndex = newFileRecords.indexOf(oldFileRecord);

			if (matchIndex < 0) {
				// placed dummy because i currently have no access to the
				// calling app's shared folder
				deleteJobs.add(new DeleteJob(Paths.get("dummy/" + oldFileRecord.getFileName()), oldRecord
						.getTimeLastModified(), serverSocket));
			}
		}

		return deleteJobs;
	}
}
