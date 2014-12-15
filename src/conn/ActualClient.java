package conn;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

import javax.swing.JFrame;

import client.DirectoryListenerThread;
import client.filerecords.ClientFileRecordManager;
import commons.Constants;
import job.manager.ClientJobManager;
import job.manager.JobManager;
import filemanager.SingleFolderFileManager;

public class ActualClient extends Client {
	private ConnectionManager connectionManager;
	private ClientFileRecordManager fileRecordManager;
	private ClientJobManager jobManager;
	private SingleFolderFileManager fileManager;
	
	public ActualClient(String serverAddr, String localFolder) {
		fileManager = new SingleFolderFileManager(localFolder);
		jobManager = new ClientJobManager(this, fileManager);
		
		connectionManager = new ConnectionManager();
		fileRecordManager = new ClientFileRecordManager(path.toString(), Constants.FOLDER_RECORD_FILENAME);

		Connection conn = attemptConnection(serverAddr);
		Thread dirListenThread = new Thread(new DirectoryListenerThread(path, conn, jobManager));
		dirListenThread.setName("Directory Listener Thread");
		dirListenThread.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutDown();
			}
		});

		JFrame frame = new JFrame();
		frame.setTitle("BoxDrive Client");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

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
				Connection connection = connectionManager.createNewConnection(socket,jobManager);
				System.out.println(socket.getRemoteSocketAddress() + " has connected.");
				return connection;
			}
		} while (socket == null);
		return null;
	}
	
	class JSONJobHandlingThread extends Thread {
		JSONJobHandlingThread(){
			this.setName("JSONThread for "+ identifier);
		}
		
		@Override
		public void run() {
			String json;

			while (true) {
				json = getConnection().read();
				jobManager.handleNewJsonMessage(json);
			}
		}
	}
}
