package conn;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
	// private static ConnectionManager instance = new ConnectionManager();

	private List<Connection> connections;

	// public static ConnectionManager getInstance() {
	// return instance;
	// }
	public ConnectionManager() {
		connections = new ArrayList<>();
	}

	public void createNewConnection(Socket socket) {
		Connection conn = new Connection(socket);
		connections.add(conn);

		this.broadcast("Hello everyone, a newcomer has joined us.");
		System.out.println(this.connections.get(0).read());
	}

	public Socket getSocket(int index) {
		if (connections.size() > index)
			return connections.get(index).socket;
		else
			return null;
	}

	public String readFrom(int index) {
		return connections.get(index).read();
	}

	public void writeTo(int index, String str) {
		connections.get(index).write(str);
	}

	public String readFromFirst() {
		return connections.get(0).read();
	}

	public void writeToFirst(String str) {
		connections.get(0).write(str);
	}

	public void broadcast(String str) {
		for (Connection conn : connections) {
			conn.write(str);
		}
	}

	// TODO: remove connection

	// private List<ClientProxy> clients = new ArrayList<>();
	//
	//
	// void newlyConnected(Socket socket) {
	// ClientProxy client = new ClientProxy(socket);
	// clients.add(client);
	// new Thread(new NewConnectionThread(client)).start();
	// }
	//
	//
	// void broadcast(AbstractClient caller, Job job) {
	// job.setForSending();
	// for (ClientProxy client : clients) {
	// if (!client.equals(caller)) {
	// System.out.println("Will broadcast job to: " + client.getSocket().getRemoteSocketAddress());
	// ServerJobManager.getInstance().handle(client, job);
	// }
	// }
	// }
	//
	//
	// void remove(ClientProxy client) {
	// clients.remove(client);
	// }
}
