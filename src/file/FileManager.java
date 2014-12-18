package file;

import java.util.List;


public interface FileManager {
	public abstract void setLastModifiedTime(FileBean file);
	public abstract boolean exists(FileBean file);
	public abstract int compareLastModifiedTime(FileBean file);
	public abstract boolean createDirectory(FileBean file);
	public abstract boolean createFile(FileBean file, String fileByteString);
	public abstract boolean hasSameContents(FileBean file);
	public abstract void delete(FileBean file);
	public abstract String getFileBytes(FileBean file);
	public abstract FileBean getFileBean(String filename);
	public abstract List<FileBean> getAllFiles();
}
