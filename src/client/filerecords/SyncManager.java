package client.filerecords;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncManager {

	private static SyncManager instance;

	public static SyncManager getInstance() {
		if (instance == null)
			instance = new SyncManager();
		return instance;
	}

	private ArrayList<FileRecord> fileRecords;
	private Lock lock = new ReentrantLock();
	private Condition isFileRecordsInitialized = lock.newCondition();

	public void clearFileRecords() {
		lock.lock();
		fileRecords = null;
		lock.unlock();
	}

	public void initFileRecords(List<FileRecord> fileRecords) {
		lock.lock();
		System.out.println("Records have been initialized!");
		this.fileRecords = new ArrayList<FileRecord>(fileRecords);
		isFileRecordsInitialized.signal();
		lock.unlock();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<FileRecord> getFileRecords() {
		lock.lock();
		ArrayList<FileRecord> clone = (ArrayList<FileRecord>) fileRecords.clone();
		lock.unlock();
		return clone;
	}

	public void waitForFileRecords() {
		lock.lock();
		try {
			System.out.println("Im gonna wait.");
			while (fileRecords == null)
				isFileRecordsInitialized.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
