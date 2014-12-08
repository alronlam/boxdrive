package commons.filerecords;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class FileRecordList {

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

}
