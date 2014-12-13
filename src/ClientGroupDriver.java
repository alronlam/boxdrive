import java.nio.file.Path;
import java.nio.file.Paths;

import client.Client;

public class ClientGroupDriver {
	public static void main(String[] args) {

		ClientDriver.main(null);
		DummyClientDriver.main(null);
	}
}
