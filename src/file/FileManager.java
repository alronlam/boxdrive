package file;


public abstract class FileManager {
	public abstract void setLastModifiedTime(FileBean file);
	public abstract boolean exists(FileBean file);
	public abstract int compareLastModifiedTime(FileBean file);
	public abstract void createDirectory(FileBean file);
	public abstract boolean createFile(FileBean file, String fileByteString);
	public abstract boolean hasSameContents(FileBean file);
	public abstract void delete(FileBean file);
	public abstract String getFileBytes(FileBean file);
	public abstract FileBean getUpdatedFileBean(FileBean file);
}
