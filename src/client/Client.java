package client;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import job.JobManager;
import client.filerecords.ClientFileRecordManager;

import commons.Constants;

import conn.Connection;
import conn.ConnectionManager;

public class Client {
	private Socket socket;
	private JobManager jobManager;
	private ClientFileRecordManager fileRecordManager;

	public static void main(String args[]) {
		new Client("localhost", Paths.get("client1"));
	}

	public Client(String serverAddr, Path path) {
		Constants.FOLDER = path.toString(); // Makeshift global. Bad.

		// ServerJobManager.getInstance().setFolder(FOLDER);

		// TODO: Change to ServerJobManager
		jobManager = new JobManager();
		fileRecordManager = new ClientFileRecordManager(path.toString(), Constants.FOLDER_RECORD_FILENAME);

		Connection conn = attemptConnection(serverAddr);
		new Thread(new DirectoryListenerThread(path, conn, jobManager)).start();
		;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutDown();
			}
		});
	}

	/***
	 * Only call this method when exiting the app
	 */
	private void shutDown() {
		fileRecordManager.serializeList();
	}

	private Connection attemptConnection(String serverAddr) {
		do {

			try {
				socket = new Socket(serverAddr, Constants.PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socket != null) {
				Connection connection = ConnectionManager.getInstance().createNewConnection(socket, jobManager);
				System.out.println(socket.getRemoteSocketAddress() + " has connected.");
				return connection;
			}
		} while (socket == null);
		return null;
	}

	public Socket getSocket(int index) {
		return ConnectionManager.getInstance().getSocket(index);
	}

	public Socket getFirstSocket() {
		return getSocket(0);
	}

}
