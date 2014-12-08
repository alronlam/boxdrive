package job;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.json.impl.Base64;

import com.hazelcast.nio.IOUtil;

import commons.Constants;

public class FileJob extends BasicJob {
	private final int BUFFER_SIZE = 8096;
	private String fileByteString;

	FileJob(JsonObject json, Socket socket) {
		super(json, socket);
		fileByteString = json.getString(Constants.Body.FILEBYTES);
	}
	
	FileJob(Path path, Socket socket) {
		super(path, socket);
		try {
			fileByteString = Base64.encodeBytes(Files.readAllBytes(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void execute() {
		// TODO handle newer existing file 
		Path localFile = file.getLocalizedFile();
		
		byte[] fileBytes = Base64.decode(fileByteString);
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
			 FileOutputStream outputStream = new FileOutputStream(localFile.toFile()))	
		{
			int read = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((read = inputStream.read(buffer)) != -1) {			
				outputStream.write(buffer, 0, read);  
			}			
			Files.setLastModifiedTime( localFile, FileTime.fromMillis(file.getLastModified()) );	
		
		} catch (IOException ex) {
			try {
				Files.delete(localFile);
			} catch (IOException ex1) {
				// something went terribly, horribly wrong
				ex1.printStackTrace();
			}
		}
	}
	
}
