import java.io.File;

public class DeleteFilesDriver {
	public static void main(String args[]) {
		File dir;
		String folders[] = { "client1", "client2", "server", "storage-server1", "storage-server2", "storage-server3" };

		for (String dirPath : folders) {
			dir = new File(dirPath);
			System.out.println("In " + dir.getName());
			for (File file : dir.listFiles()) {
				System.out.println("\tDeleting " + file.getName());
				file.delete();
			}
			System.out.println();
		}

		System.out.println("Done");
	}
}
