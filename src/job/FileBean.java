package job;

class FileBean {
	private String filename;
	private long lastModified;
	private byte[] checksum;
	private boolean isDirectory = false;
	
	protected String getFilename() {
		return filename;
	}
	
	protected long getLastModified() {
		return lastModified;
	}
	
	protected byte[] getChecksum() {
		return checksum;
	}
	
	protected boolean isDirectory() {
		return isDirectory;
	}
}
