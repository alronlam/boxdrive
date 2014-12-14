package client.filerecords;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

public class SyncManager {

	private static SyncManager instance;

	public static SyncManager getInstance() {
		if (instance == null)
			instance = new SyncManager();
		return instance;
	}

	private ArrayList<FileRecord> fileRecords;
	private Condition isFileRecordsInitialized;

	public synchronized void clearFileRecords() {
		fileRecords = null;
	}

	public synchronized void initFileRecords(List<FileRecord> fileRecords) {
		this.fileRecords = new ArrayList<FileRecord>(fileRecords);
		isFileRecordsInitialized.signal();
	}

	@SuppressWarnings("unchecked")
	public synchronized ArrayList<FileRecord> getFileRecords() {
		return (ArrayList<FileRecord>) fileRecords.clone();
	}

	public synchronized void waitForFileRecords() {
		try {
			while (fileRecords == null)
				isFileRecordsInitialized.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
