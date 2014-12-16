import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import commons.Constants;

public class ClearSerFilesDriver {
	public static void main(String args[]) throws FileNotFoundException {
		File dir;
		String clients[] = { "client1", "client2" };

		for (String dirPath : clients) {
			dir = new File(dirPath + "_" + Constants.FOLDER_RECORD_FILENAME);
			PrintWriter writer = new PrintWriter(dir);
			writer.close();

			System.out.println("Cleared contents of " + dir.getName());

		}

		String folders[] = { "client1", "client2", "storage-server1", "storage-server2", "storage-server3" };

		for (String dirPath : folders) {
			dir = new File(dirPath);
			purgeDirectory(dir);

			System.out.println("Cleared contents of " + dir.getName());

		}

		System.out.println("Done");
	}

	static void purgeDirectory(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				purgeDirectory(file);
			file.delete();
		}
	}
}
