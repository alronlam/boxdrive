package client.filerecords;

import java.util.ArrayList;
import java.util.List;

public class SyncManager {

	private static SyncManager instance;

	public static SyncManager getInstance() {
		if (instance == null)
			instance = new SyncManager();
		return instance;
	}

	private ArrayList<FileRecord> fileRecords;

	public synchronized void clearFileRecords() {
		fileRecords = null;
	}

	public synchronized void initFileRecords(List<FileRecord> fileRecords) {
		this.fileRecords = new ArrayList<FileRecord>(fileRecords);
	}

	@SuppressWarnings("unchecked")
	public synchronized ArrayList<FileRecord> getFileRecords() {
		return (ArrayList<FileRecord>) fileRecords.clone();
	}
}
