package commons.filerecords;

import java.util.ArrayList;
import java.util.Collections;

public class FolderRecord {

	// Singleton-related variables and methods

	private static FolderRecord instance = new FolderRecord();

	public static FolderRecord getInstance() {
		return instance;
	}

	private FolderRecord() {

	}

	// The rest of the class

	private ArrayList<FileRecord> list = new ArrayList<FileRecord>();
	private long timeLastModified = 0; // time this folder record was last
										// updated. this is just an internal
										// time keeper used for when deletes
										// happen while offline

	public void handleCreateOrModify(String fileName, long dateTimeModified) {
		FileRecord targetRecord = this.retrieveFileRecord(fileName);

		if (targetRecord == null)
			this.create(fileName, dateTimeModified);
		else
			this.modify(targetRecord, dateTimeModified);
	}

	private void create(String fileName, long dateTimeModified) {
		this.timeLastModified = System.currentTimeMillis();

		FileRecord newRecord = new FileRecord(fileName, dateTimeModified);
		list.add(newRecord);
		Collections.sort(list);
	}

	private void modify(FileRecord targetRecord, long dateTimeModified) {

		this.timeLastModified = System.currentTimeMillis();

		if (targetRecord == null)
			return;

		targetRecord.setDateTimeModified(dateTimeModified);
	}

	public void delete(String fileName, long dateTimeModified) {

		this.timeLastModified = System.currentTimeMillis();

		FileRecord targetRecord = this.retrieveFileRecord(fileName);

		if (targetRecord == null)
			return;

		list.remove(targetRecord);
	}

	private FileRecord retrieveFileRecord(String fileName) {

		FileRecord tempRecord = new FileRecord(fileName, 0);
		// Overrode the equals method in FileRecord, so this should return the
		// index of a file that has the specified fileName
		int index = list.indexOf(tempRecord);

		if (fileName == null)
			return null;

		return list.get(index);

	}

	@SuppressWarnings("unchecked")
	public ArrayList<FileRecord> getList() {
		return (ArrayList<FileRecord>) list.clone();
	}

	public long getTimeLastModified() {
		return timeLastModified;
	}

}
