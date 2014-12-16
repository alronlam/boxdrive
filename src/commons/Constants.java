package commons;

public class Constants {
	// Temporary constant until client handles folder name
	public static String FOLDER = "";
	public static byte[] EOF = "\r\n\r\n".getBytes();
	public static int PORT = 8080;
	public static int COORDINATOR_PORT = 10;
	
	public static String FOLDER_RECORD_FILENAME = "folderRecord.ser";
	
	/**
	 * JSON Fields.
	 */
	public class JSON {
		public static final String TYPE = "type";
		public static final String BODY = "body";	
	}
	
	/**
	 * JSON Body keys.
	 */
	public class Body {
		public static final String FILENAME = "filename";
		public static final String LAST_MODIFIED = "last modified";
		public static final String CHECKSUM = "checksum";
		public static final String IS_DIRECTORY = "is directory";
		public static final String FILEBYTES = "file bytes";
	}
	
	/**
	 * Job Types.
	 */
	// Convert to Enum?
	public class Type {
		public static final String CREATE = "create";
		public static final String DELETE = "delete";
		public static final String FILE = "file";
		public static final String LIST = "list";
		public static final String REQUEST = "request";
		public static final String CONFIG = "config";
	}
	
	public class Config {
		public static final String CLIENT_TYPE = "client type";
		public static final String ACTUAL = "actual";
		
		
		public static final String SERVER_CONFIGURATION = "server configuration";
		public static final String SERVER_NUMBER = "server number";
	}
}
