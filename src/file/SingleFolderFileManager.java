package file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.vertx.java.core.json.impl.Base64;

import file.filerecords.FolderRecord;

public class SingleFolderFileManager implements FileManager {
	private final int BUFFER_SIZE = 8096;
	private final String RECORD_FILENAME = "record.ser";
	private String localFolder;
	private int localPathNests;
	private long lastModifiedTime;
	
	
	
	public SingleFolderFileManager(String localFolder) {
		this.localFolder = localFolder;
		// Cheap hack to check number of back slashes
		localPathNests = localFolder.length() - localFolder.replace("/", "").length(); 
	}
	
	@Override
	public void setLastModifiedTime(FileBean file) {
		Path localFile = this.getLocalizedFile(file.getFilename());
		try {
			Files.setLastModifiedTime(localFile, FileTime.fromMillis(file.getLastModified()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean exists(FileBean file) {
		Path localFile = this.getLocalizedFile(file.getFilename());
		return Files.exists(localFile);
	}

	
	/**
	 * Compares the last modified times of the received file with an existing file.
	 * 
	 * @param file
	 *            A localized Path to the other file.
	 * @return 0 if this file is modified at the same time as other, a value less than 0 if this file is older than other, and a value greater than 0 if this file is newer than
	 *         other.
	 */
	@Override
	public int compareLastModifiedTime(FileBean file) {
		Path localFile = this.getLocalizedFile(file.getFilename());
		
		int comparison = -1;
		try {

			long difference = Files.getLastModifiedTime(localFile).toMillis() - file.getLastModified();

			if (difference > 0)
				return 1;

			else if (difference < 0)
				return -1;

			else
				return 0;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return comparison;


	}

	@Override
	public boolean createDirectory(FileBean file) {
		boolean success = false;
		Path localFile = this.getLocalizedFile(file.getFilename());
		try {
			Files.createDirectory(localFile);
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
	}

	@Override
	public boolean createFile(FileBean file, String fileByteString) {
		boolean success = false;
		Path localFile = this.getLocalizedFile(file.getFilename());
		byte[] fileBytes = Base64.decode(fileByteString);
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
				FileOutputStream outputStream = new FileOutputStream(localFile.toFile())) {
			int read = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((read = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, read);
			}
			inputStream.close();
			outputStream.close();
			Files.setLastModifiedTime(localFile, FileTime.fromMillis(file.getLastModified()));
			success = true;
		} catch (IOException ex) {
			try {
				Files.delete(localFile);
				System.err.println("Error. Deleting: " + localFile.toString());
			} catch (IOException ex1) {
				// something went terribly, horribly wrong
				ex1.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public String getFileBytes(FileBean file) {
		String fileBytes = "";
		Path localFile = this.getLocalizedFile(file.getFilename());
		
		try {
			fileBytes = Base64.encodeBytes(Files.readAllBytes(localFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileBytes;
	}

	@Override
	public boolean hasSameContents(FileBean other) {
		Path localFile = this.getLocalizedFile(other.getFilename());
		
		byte[] checksum = getChecksum(localFile.toString());
		byte[] otherChecksum = other.getChecksum();
		return Arrays.equals(checksum, otherChecksum);
	}

	@Override
	public void delete(FileBean file) {
		Path localFile = this.getLocalizedFile(file.getFilename());
		try {
			this.deleteRecursive(localFile.toFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private Path getLocalizedFile(String filename) {
		return Paths.get(localFolder, filename);
	}
	
	
	/**
	 * {@link http://stackoverflow.com/a/4026761/2247074}
	 * 
	 * @throws FileNotFoundException
	 */
	private synchronized boolean deleteRecursive(File path) throws FileNotFoundException {
		if (!path.exists())
			throw new FileNotFoundException(path.getAbsolutePath());
		boolean ret = true;
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				ret = ret && deleteRecursive(f);
			}
		}
		return ret && path.delete();
	}

	@Override
	public FileBean getFileBean(String filename) {
		Path localFile = this.getLocalizedFile(filename);
		return this.getFileBeanFromLocalized(localFile);
	}
	
	private FileBean getFileBeanFromLocalized(Path localFile) {
		long lastModified = 0;
		boolean isDirectory = Files.isDirectory(localFile);
		byte[] checksum = null;
		if (!isDirectory) {
			checksum = getChecksum(localFile.toString());
			try {
				lastModified = Files.getLastModifiedTime(localFile).toMillis();
			} catch (IOException e) {}
		}
		
		Path delocalizedFile = this.delocalize(localFile);
		return new FileBean(delocalizedFile.toString(), lastModified, checksum, isDirectory);
	}
	
	private byte[] getChecksum(String filename) {
		try {
			InputStream fis =  new FileInputStream(filename);

			byte[] buffer = new byte[1024];
			MessageDigest complete = null;
			
			complete = MessageDigest.getInstance("MD5");
			int numRead;
	
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			
			fis.close();
			return complete.digest();
		} catch (NoSuchAlgorithmException ex) {
		} catch (IOException ex) {}
		
		return null;	
   	
	}
	
	private Path delocalize(Path localFile) {
		return localFile.subpath(1 + localPathNests, localFile.getNameCount());
	}

	@Override
	public List<FileBean> getAllFiles() {
		List<FileBean> files = new ArrayList<>();
		
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.getLocalizedFile(""))) {
			for (Path path : directoryStream) {
				FileBean file = this.getFileBeanFromLocalized(path);
				
				files.add(file);
			}
			Collections.sort(files);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return files;
	}

	@Override
	public void serializeFiles() {
		FolderRecord folderRecord = new FolderRecord();
		folderRecord.setList(this.getAllFiles());
		folderRecord.setTimeLastModified(System.currentTimeMillis());
		
		try (OutputStream file = new FileOutputStream( this.getLocalizedFile(RECORD_FILENAME).toString() );
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);) {
			output.writeObject(folderRecord);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("\nCurrent record is now\n " + this.toString() + "\n");
		
	}

	@Override
	public List<FileBean> getSerializedFiles() {
		try (InputStream file = new FileInputStream( this.getLocalizedFile(RECORD_FILENAME).toString() );
				InputStream buffer = new BufferedInputStream(file);
				ObjectInput input = new ObjectInputStream(buffer);) {
			
			FolderRecord folderRecord = (FolderRecord) input.readObject();
			this.lastModifiedTime = folderRecord.getTimeLastModified();
			return folderRecord.getList();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public long getLastModifiedTime() {
		return this.lastModifiedTime;
	}
	
}