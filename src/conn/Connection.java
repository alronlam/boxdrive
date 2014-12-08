package conn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Connection {
	Socket socket;
	InputStream inStream;
	OutputStream outStream;
	Semaphore awaitMessage;
	Semaphore messageMutex;
	ArrayList<String> msgQueue;

	public Connection(Socket socket) {
		this.socket = socket;

		try {
			inStream = socket.getInputStream();
			outStream = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		awaitMessage = new Semaphore(0);
		messageMutex = new Semaphore(1);
		msgQueue = new ArrayList<>();
		new ReadThread().start();
	}

	public void write(String str) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(out);

			os.writeObject(str);
			this.write(out.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void write(byte bytes[]) {
		try {
			outStream.write(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String read() {
		String str = null;
		try {
			awaitMessage.acquire();
			messageMutex.acquire();

			str = msgQueue.remove(0);

			messageMutex.release();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str;
	}

	public void cancel() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class ReadThread extends Thread {

		@Override
		public void run() {
			ObjectInputStream is = null;

			// TODO: reading

			try {
				is = new ObjectInputStream(inStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (true) {

				String str = null;

				try {
					str = (String) is.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (str != null) {
					try {
						messageMutex.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					msgQueue.add(str);

					messageMutex.release();

					awaitMessage.release();
				}
			}
		}

	}
}
