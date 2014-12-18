package file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.manager.StorageServerManager;

public class MultiServerFileManager implements FileManager {
	private StorageServerManager serverManager;
	private Map<String, FileBeanServer> fileBeanMap = new HashMap<>();  
	
	public MultiServerFileManager(StorageServerManager serverManager) {
		this.serverManager = serverManager;
	}
	
	/**
	 * Updates the stored FileBean. Doesn't modify the actual file in the 
	 * storage servers.
	 * @param file
	 */
	@Override
	public void setLastModifiedTime(FileBean file) {
		FileBean storedFile = this.getStoredFileBean(file);
		storedFile.setLastModified(file.getLastModified());
	}

	@Override
	public boolean exists(FileBean file) {
		return fileBeanMap.containsKey(file.getFilename());
	}

	@Override
	public int compareLastModifiedTime(FileBean file) {
		FileBean storedFile = getStoredFileBean(file);
		
		long difference = storedFile.getLastModified() - file.getLastModified();
		
		if (difference > 0)
			return 1;
		
		else if (difference < 0)
			return -1;
		
		else
			return 0;
	}

	@Override
	public boolean createDirectory(FileBean file) {
		int serverNumber = this.getServerNumber(file);
		return serverManager.createDirectory(file, serverNumber);
	}
	
	@Override
	public boolean createFile(FileBean file, String fileByteString) {
		int serverNumber;
		if (this.exists(file)) {
			serverNumber = this.getServerNumber(file);
		} else {
			serverNumber = serverManager.getNewServerNumber();
		}
		
		boolean success = serverManager.createFile(file, fileByteString, serverNumber);
		if (success) {
			FileBeanServer fbs = new FileBeanServer(file, serverNumber);
			fileBeanMap.put(file.getFilename(), fbs);
		}
		
		return success;
	}

	@Override
	public boolean hasSameContents(FileBean otherFile) {
		FileBean localFile = this.getStoredFileBean(otherFile);
		return Arrays.equals(localFile.getChecksum(), otherFile.getChecksum());
	}

	@Override
	public void delete(FileBean file) {
		int serverNumber = this.getServerNumber(file);
		boolean success = serverManager.delete(file, serverNumber);
		if (success) {
			fileBeanMap.remove(file.getFilename());
		}
	}

	@Override
	public String getFileBytes(FileBean file) {
		int serverId = this.getServerNumber(file);
		return serverManager.getFileBytes(file, serverId);
	}

	@Override
	public FileBean getFileBean(String filename) {
		return fileBeanMap.get(filename).file;
	}
	
	private FileBean getStoredFileBean(FileBean file) {
		return fileBeanMap.get(file.getFilename()).file;
	}
	
	private int getServerNumber(FileBean file) {
		return fileBeanMap.get(file.getFilename()).serverNumber;
	}

	private class FileBeanServer {
		private FileBean file;
		private int serverNumber;
		private FileBeanServer(FileBean file, int serverNumber) {
			this.file = file;
			this.serverNumber = serverNumber;
		}
	}

	@Override
	public List<FileBean> getAllFiles() {
		ArrayList<FileBean> files = new ArrayList<>();
		for (FileBeanServer fbs : fileBeanMap.values()) {
			files.add(fbs.file);
		}
		Collections.sort(files);
		
		return files;
	}
	
	@Override @Deprecated
	public void serializeFiles() {}

	@Override @Deprecated
	public List<FileBean> getSerializedFiles() {
		return new ArrayList<>();
	}

	@Override @Deprecated
	public long getLastModifiedTime() {
		return 0;
	}
}