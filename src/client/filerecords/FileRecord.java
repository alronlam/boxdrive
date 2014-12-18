package client.filerecords;

import java.io.Serializable;
import java.util.Date;

/***
 * Represents a record of one file in the shared folder. Do not give out a
 * setter for filename because it's not supposed to change. If files are
 * renamed, we treat them as: delete the old file and create a new file with the
 * new name.
 * 
 * @author user
 * 
 */
public class FileRecord implements Comparable<FileRecord>, Serializable {

	private String fileName;
	private long dateTimeModified;

	public FileRecord(String fileName, long dateTimeModified) {
		this.fileName = fileName;
		this.dateTimeModified = dateTimeModified;
	}

	public long getDateTimeModified() {
		return dateTimeModified;
	}

	public void setDateTimeModified(long dateTimeModified) {
		this.dateTimeModified = dateTimeModified;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
	public int compareTo(FileRecord o) {
		return fileName.compareTo(o.fileName);
	}

	@Override
	public boolean equals(Object other) {
		FileRecord o = (FileRecord) other;
		return fileName.equals(o.fileName);
	}

	public String toString() {

		Date time = new Date(dateTimeModified);
		return fileName + " - " + time.toString();

	}

}
