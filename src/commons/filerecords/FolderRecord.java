package commons.filerecords;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class FolderRecord {

	private ArrayList<FileRecord> list = new ArrayList<FileRecord>();
	private long timeLastModified = 0; // time this folder record was last
										// updated

	public void create(String fileName, long dateTimeModified) {
		timeLastModified = System.currentTimeMillis();
		FileRecord newRecord = new FileRecord(fileName, dateTimeModified);
		list.add(newRecord);
		Collections.sort(list);
	}

	public void modify(String fileName, long dateTimeModified) {

		timeLastModified = System.currentTimeMillis();

		FileRecord targetRecord = this.retrieveFileRecord(fileName);

		if (targetRecord == null)
			return;

		targetRecord.setDateTimeModified(dateTimeModified);
	}

	public void delete(String fileName, Calendar dateTimeModified) {

		timeLastModified = System.currentTimeMillis();

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
