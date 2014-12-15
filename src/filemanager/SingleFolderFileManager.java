package filemanager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import org.vertx.java.core.json.impl.Base64;

public class SingleFolderFileManager extends FileManager {
	private String localfolder;
	private final int BUFFER_SIZE = 8096;
	
	public SingleFolderFileManager(String localfolder) {
		this.localfolder = localfolder;
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

	@Override
	public int compareLastModifiedTime(FileBean file) {
		Path localFile = this.getLocalizedFile(file.getFilename());
		return file.compareLastModifiedTime(localFile);
	}

	@Override
	public void createDirectory(FileBean file) {
		Path localFile = this.getLocalizedFile(file.getFilename());
		try {
			Files.createDirectory(localFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				System.out.println("wrting: " + buffer);
			}
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
	public boolean hasSameContents(FileBean file) {
		Path localFile = this.getLocalizedFile(file.getFilename());
		return file.hasSameContents(localFile);
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
		return Paths.get(localfolder, filename);
	}
	
	
	/**
	 * {@link http://stackoverflow.com/a/4026761/2247074}
	 * 
	 * @throws FileNotFoundException
	 */
	private synchronized boolean deleteRecursive(File path) throws FileNotFoundException {
		System.out.println("Deleting: " + path.toString());

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
	public FileBean getUpdatedFileBean(FileBean file) {
		Path localFile = this.getLocalizedFile(file.getFilename());
		Path delocalizedFile = localFile.subpath(1, localFile.getNameCount());
		return new FileBean(delocalizedFile);
	}
}
