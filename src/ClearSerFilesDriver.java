import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import commons.Constants;


public class ClearSerFilesDriver {
	public static void main(String args[]) throws FileNotFoundException {
		File dir;
		String folders[] = { "client1","client2" };

		for (String dirPath : folders) {
			dir = new File(dirPath+ "_" +  Constants.FOLDER_RECORD_FILENAME);
			PrintWriter writer = new PrintWriter(dir);
			writer.close();
			
			System.out.println("Cleared contents of "+ dir.getName());
		}

		System.out.println("Done");
	}
}
