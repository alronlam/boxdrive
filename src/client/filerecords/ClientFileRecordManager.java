package client.filerecords;

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
import java.util.Date;

public class ClientFileRecordManager {

	private final String RECORD_PATH;
	private String sharedFolderName;

	public ClientFileRecordManager(String sharedFolderName, String recordFileName) {

		this.sharedFolderName = sharedFolderName;
		this.RECORD_PATH = sharedFolderName + "_" + recordFileName;

		// try to read from serialized list
		// if not successful, then init the records based on directory
		ArrayList<FileRecord> records = this.readFromSerializedList(this.RECORD_PATH);
		System.out.println("Read File	  Records: " + records);
		if (records == null)
			records = this.initRecordsBasedOnDirectory(sharedFolderName);

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

	public void serializeList() {

		this.list = this.initRecordsBasedOnDirectory(this.sharedFolderName);

		// serialize the List
		try (OutputStream file = new FileOutputStream(RECORD_PATH);
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);) {
			output.writeObject(this.list);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("\nCurrent record is now\n " + this.toString() + "\n");

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

		if (index < 0)
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

	public String toString() {
		StringBuilder sb = new StringBuilder("Record as of " + new Date(this.timeLastModified).toString());

		for (FileRecord record : list) {
			sb.append(record.toString()).append("\n");
		}

		return sb.toString();
	}
}
