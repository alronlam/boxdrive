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

public class ClientFileRecordManager {

	private final String RECORD_PATH;
	private String sharedFolderName;

	public ClientFileRecordManager(String sharedFolderName, String recordFileName) {

		this.sharedFolderName = sharedFolderName;
		this.RECORD_PATH = sharedFolderName + "_" + recordFileName;

		// try to read from serialized list
		// if not successful, then init the records based on directory
		this.folderRecord = this.readFromSerializedList(this.RECORD_PATH);

		if (folderRecord == null)
			folderRecord = new FolderRecord();// this.generateRecordBasedOnCurrentDirectoryState();

		System.out.println("Read File Records: " + folderRecord.toString());
	}

	private FolderRecord readFromSerializedList(String filePath) {

		try (InputStream file = new FileInputStream(filePath);
				InputStream buffer = new BufferedInputStream(file);
				ObjectInput input = new ObjectInputStream(buffer);) {
			// deserialize the List
			FolderRecord folderRecord = (FolderRecord) input.readObject();

			return folderRecord;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public void serializeList() {

		folderRecord.setList(this.generateRecordBasedOnCurrentDirectoryState());
		folderRecord.setTimeLastModified(System.currentTimeMillis());

		// serialize the List
		try (OutputStream file = new FileOutputStream(RECORD_PATH);
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);) {
			output.writeObject(folderRecord);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("\nCurrent record is now\n " + this.toString() + "\n");

	}

	public ArrayList<FileRecord> generateRecordBasedOnCurrentDirectoryState() {

		ArrayList<FileRecord> records = new ArrayList<FileRecord>();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(this.sharedFolderName))) {
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

	private ArrayList<FileRecord> updateRecordsTimeModified(ArrayList<FileRecord> fileRecords, String directory) {

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {

			for (FileRecord fileRecord : fileRecords) {
				Path path = this.getPathFromDirectoryStream(directoryStream, fileRecord.getFileName());

				if (path != null)
					fileRecord.setDateTimeModified(Files.getLastModifiedTime(path).toMillis());

			}

			Collections.sort(fileRecords);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return fileRecords;
	}

	private Path getPathFromDirectoryStream(DirectoryStream<Path> directoryStream, String filename) {
		for (Path path : directoryStream) {
			if (path.getFileName().toString().equals(filename))
				return path;
		}

		return null;
	}

	// The rest of the class

	private FolderRecord folderRecord;

	public ArrayList<FileRecord> getList() {
		return folderRecord.getList();
	}

	public long getTimeLastModified() {
		return folderRecord.getTimeLastModified();
	}

	public String toString() {
		return folderRecord.toString();
	}
}
