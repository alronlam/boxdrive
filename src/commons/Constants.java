package commons;

public class Constants {
	// Temporary constant until client handles folder name
	public static String FOLDER = "";
	public static byte[] EOF = "\r\n\r\n".getBytes();
	public static int PORT = 8080;
	public static int COORDINATOR_PORT = 10;
	
	/**
	 * JSON Fields.
	 */
	public class JSON {
		public final static String TYPE = "type";
		public final static String BODY = "body";	
	}
	
	/**
	 * JSON Body keys.
	 */
	public class Body {
		public final static String FILENAME = "filename";
		public final static String LAST_MODIFIED = "last modified";
		public final static String CHECKSUM = "checksum";
		public final static String IS_DIRECTORY = "is directory";
		public final static String FILEBYTES = "file bytes";
	}
	
	/**
	 * Job Types.
	 */
	// Convert to Enum?
	public class Type {
		public final static String CREATE = "create";
		public final static String DELETE = "delete";
		public final static String FILE = "file";
		public final static String LIST = "list";
		public final static String REQUEST = "request";
	}
}
