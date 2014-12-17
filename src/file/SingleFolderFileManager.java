package file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.vertx.java.core.json.impl.Base64;

public class SingleFolderFileManager implements FileManager {
	private String localFolder;
	private final int BUFFER_SIZE = 8096;
	private int localPathNests;
	
	
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
	
}
