package commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	public static byte[] getChecksum(String filename) {
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
}
