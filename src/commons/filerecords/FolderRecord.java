package commons.filerecords;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import job.Job;

public class FolderRecord {

	private ArrayList<FileRecord> list = new ArrayList<FileRecord>();

	public void create(String fileName, Calendar dateTimeModified) {
		FileRecord newRecord = new FileRecord(fileName, dateTimeModified);
		list.add(newRecord);
		Collections.sort(list);
	}

	public void modify(String fileName, Calendar dateTimeModified) {
		FileRecord targetRecord = this.retrieveFileRecord(fileName);

		if (targetRecord == null)
			return;

		targetRecord.setDateTimeModified(dateTimeModified);
	}

	public void delete(String fileName, Calendar dateTimeModified) {
		FileRecord targetRecord = this.retrieveFileRecord(fileName);

		if (targetRecord == null)
			return;

		list.remove(targetRecord);
	}

	private FileRecord retrieveFileRecord(String fileName) {

		FileRecord tempRecord = new FileRecord(fileName, null);
		// Overrode the equals method in FileRecord, so this should return the
		// index of a file that has the specified fileName
		int index = list.indexOf(tempRecord);

		if (fileName == null)
			return null;

		return list.get(index);

	}

	/***
	 * This object is considered as the latest record, while the otherFolder is
	 * considered as the older record.
	 * 
	 * @param otherFolder
	 * @return
	 */
	public ArrayList<Job> compareAndGenerateJobs(FolderRecord otherFolder) {
		ArrayList<Job> jobs = new ArrayList<Job>();
		jobs.addAll(this.generateCreateJobs(otherFolder));
		jobs.addAll(this.generateModifyJobs(otherFolder));
		jobs.addAll(this.generateDeleteJobs(otherFolder));
		return jobs;
	}

	private ArrayList<Job> generateCreateJobs(FolderRecord otherFolder) {
		ArrayList<Job> createJobs = new ArrayList<Job>();

		return createJobs;
	}

	private ArrayList<Job> generateModifyJobs(FolderRecord otherFolder) {
		ArrayList<Job> modifyJobs = new ArrayList<Job>();

		return modifyJobs;
	}

	private ArrayList<Job> generateDeleteJobs(FolderRecord otherFolder) {
		ArrayList<Job> deleteJobs = new ArrayList<Job>();

		return deleteJobs;
	}

}
