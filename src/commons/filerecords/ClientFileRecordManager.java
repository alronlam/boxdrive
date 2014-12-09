package commons.filerecords;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class ClientFileRecordManager {

	// Singleton-related variables and methods

	private static ClientFileRecordManager instance = new ClientFileRecordManager();

	public static ClientFileRecordManager getInstance() {
		return instance;
	}

	private ClientFileRecordManager() {

		String directory = "client1"; // temporary. will need to access
										// the client's directory
		String recordFileName = "file_records.ser";

		// try to read from serialized list
		// if not successful, then init the records based on directory
		ArrayList<FileRecord> records = this.readFromSerializedList(recordFileName);
		if (records == null)
			records = this.initRecordsBasedOnDirectory(directory);

	}

	private ArrayList<FileRecord> readFromSerializedList(String filePath) {
		// deserialize the quarks.ser file
		try (InputStream file = new FileInputStream(filePath);
				InputStream buffer = new BufferedInputStream(file);
				ObjectInput input = new ObjectInputStream(buffer);) {
			// deserialize the List
			ArrayList<FileRecord> records = (ArrayList<FileRecord>) input.readObject();

			return records;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private void serializeList(String filePath) {
		// serialize the List
		try (OutputStream file = new FileOutputStream(filePath);
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);) {
			output.writeObject(this.list);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private ArrayList<FileRecord> initRecordsBasedOnDirectory(String directory) {

		ArrayList<FileRecord> records = new ArrayList<FileRecord>();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
			for (Path path : directoryStream) {
				FileRecord fileRecord = new FileRecord(path.getFileName().toString(), Files.getLastModifiedTime(path)
						.toMillis());
				records.add(fileRecord);
			}
			Collections.sort(records);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return records;
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
