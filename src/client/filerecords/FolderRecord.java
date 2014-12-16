package client.filerecords;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class FolderRecord implements Serializable {
	private ArrayList<FileRecord> list = new ArrayList<FileRecord>();
	private long timeLastModified = 0; // time this folder record was last
										// updated. this is just an internal
										// time keeper used for when deletes
										// happen while offline

	public long getTimeLastModified() {
		return timeLastModified;
	}

	public void setTimeLastModified(long timeLastModified) {
		this.timeLastModified = timeLastModified;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<FileRecord> getList() {
		return (ArrayList<FileRecord>) list.clone();
	}

	public void setList(ArrayList<FileRecord> list) {
		this.list = list;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Record as of " + new Date(this.timeLastModified).toString());

		for (FileRecord record : list) {
			sb.append(record.toString()).append("\n");
		}

		return sb.toString();
	}

}
