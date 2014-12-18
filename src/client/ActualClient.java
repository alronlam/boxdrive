package client;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFrame;

import actualclient.DirectoryListenerThread;
import commons.Constants;
import job.ConfigJob;
import job.Job;
import job.ListJob;
import job.manager.ClientJobManager;
import file.FileBean;
import file.SingleFolderFileManager;
import file.filerecords.SharedFolderRecordComparator;
import file.filerecords.SyncManager;

public class ActualClient extends Client {
	private SingleFolderFileManager fileManager;
	
	public ActualClient(String serverAddr, String localFolder) {
		System.out.println("client initialized");
		fileManager = new SingleFolderFileManager(localFolder);
		this.setJobManager(new ClientJobManager(fileManager));
		
		// fileRecordManager = new ClientFileRecordManager(path.toString(), Constants.FOLDER_RECORD_FILENAME);
		
		this.setConnection( this.attemptConnection(serverAddr));
		
		ConfigJob configuration = ConfigJob.getActual();
		getConnection().write(configuration.getJson());
		
		this.listenForJobs();
		
		Path localPath = Paths.get(localFolder);
		Thread dirListenThread = new Thread(
				new DirectoryListenerThread(localPath, this, this.getJobManager(), fileManager));
		dirListenThread.setName("Directory Listener Thread");
		dirListenThread.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutDown();
			}
		});

		this.syncWithCoordinator();
		
		JFrame frame = new JFrame();
		frame.setTitle("BoxDrive Client");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		// frame.setVisible(true);
	}

	/***
	 * Only call this method when exiting the app
	 */
	private void shutDown() {
		fileManager.serializeFiles();
	}

	private Connection attemptConnection(String serverAddr) {
		Socket socket = null;
		do {
			System.out.println("Client trying to connect;");
			try {
				socket = new Socket(serverAddr, Constants.PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (socket != null) {
				Connection connection = new Connection(socket);
				System.out.println(socket.getRemoteSocketAddress() + " has connected.");
				return connection;
			}
		} while (socket == null);
		return null;
	}
	
    private void syncWithCoordinator() {
        SyncManager.getInstance().clearFileRecords();

        Job listJob = new ListJob();
        this.getJobManager().handleNewJob(listJob, this);
        
        // wait in this monitor until the result has returned
        SyncManager.getInstance().waitForFileRecords();

        // Compare with last record and generate appropriate jobs
        SharedFolderRecordComparator comparator = new SharedFolderRecordComparator();
        List<FileBean> newFileRecords = SyncManager.getInstance().getFileRecords();
        List<FileBean> currFileRecords = fileManager.getAllFiles();
        List<FileBean> oldFileRecords = fileManager.getSerializedFiles();
        long lastTimeOldRecordsModified = fileManager.getLastModifiedTime();

        List<Job> newJobs = comparator.compareAndGenerateJobs(newFileRecords, currFileRecords, oldFileRecords,
                lastTimeOldRecordsModified);

        System.out.println("\nSYNC RESULTS:");
        for (Job newJob : newJobs) {
            System.out.println(newJob.getJson());
            getJobManager().handleNewJob(newJob, this);
        }

        System.out.println("SYNC DONE:\n");
    }
}
