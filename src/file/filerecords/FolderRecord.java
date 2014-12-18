package file.filerecords;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import file.FileBean;

public class FolderRecord implements Serializable {
	/**/
	private static final long serialVersionUID = 7154665561296902453L;
	private List<FileBean> list = new ArrayList<>();
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

	public List<FileBean> getList() {
		return new ArrayList<>(list);
	}

	public void setList(List<FileBean> list) {
		this.list = list;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Record as of " + new Date(this.timeLastModified).toString());

		for (FileBean record : list) {
			sb.append(record.toString()).append("\n");
		}

		return sb.toString();
	}

}
