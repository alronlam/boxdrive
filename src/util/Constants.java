package util;

public class Constants {
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
