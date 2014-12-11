package conn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import job.JobManager;

public class Connection {
	// Socket-related variables
	public Socket socket;
	
	// Message-related variables
	private ArrayList<String> msgQueue;
	private Semaphore awaitMessage;
	private Semaphore messageMutex;
	
	private JobManager jobManager;
	
	public Connection(Socket socket, JobManager jobManager) {
		this.socket = socket;

		awaitMessage = new Semaphore(0);
		messageMutex = new Semaphore(1);
		msgQueue = new ArrayList<>();
		
		this.jobManager = jobManager;

		new ReadThread().start();
		new JSONJobHandlingThread().start();
	}

	/**
	 * Sends string over to connected peer through the socket
	 * 
	 * @param msg
	 *            String to be sent to peer
	 */
	public void write(String msg) {
		System.out.println("Sending: ");
		System.out.println(msg);
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(msg);
			this.write(out.toByteArray());
		} catch (IOException e) {
			System.err.println("Failed to set up streams for writing String to socket's stream.");
		}
	}

	/**
	 * Sends byte data over to peer through the socket
	 * 
	 * @param bytes
	 *            Array of Bytes, each of which would be sent to the peer
	 */
	public void write(byte bytes[]) {
		try {
			OutputStream outStream = socket.getOutputStream();
			outStream.write(bytes);
		} catch (IOException e) {
			System.err.println("Failed to write byte array to stream.");
		}
	}

	/**
	 * Retrieves the next message that was received by the host
	 * 
	 * @return returns the first message that is read from the message buffer
	 */
	public String read() {
		String str = null;
		try {
			awaitMessage.acquire();
			messageMutex.acquire();
			str = msgQueue.remove(0);
			messageMutex.release();
		} catch (InterruptedException e) {
			System.err.println("Failed to resolve concurrency for message queue.");
		}

		return str;
	}

	/**
	 * Closes the socket and thus ends the connection to the peer
	 */
	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Failed to close the socket.");
		}
	}

	class ReadThread extends Thread {

		@Override
		public void run() {
			while (true) {

				String str = null;

				try {
					InputStream inStream = socket.getInputStream();
					ObjectInputStream is = new ObjectInputStream(inStream);
					str = (String) is.readObject();
					System.out.println("Received: ");
					System.out.println(str);
					
				} catch (ClassNotFoundException e) {
					System.err.println("Failed to parse stream data as String.");
				} catch (IOException e) {
					System.err.println("Failed to read from socket stream.");
				}

				if (str != null) {
					try {
						messageMutex.acquire();
					} catch (InterruptedException e) {
						System.err.println("Failed to resolve concurrency for message queue.");
					}
					msgQueue.add(str);
					messageMutex.release();
					awaitMessage.release();
				}
			}
		}
	}

	class JSONJobHandlingThread extends Thread {
		@Override
		public void run() {
			String json;

			while (true) {
				json = read();
				jobManager.handleNewJsonMessage(json, Connection.this);
			}
		}
	}
}
