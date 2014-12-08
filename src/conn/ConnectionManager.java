package conn;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
	private static ConnectionManager instance = new ConnectionManager();

	private List<Connection> connections = new ArrayList<>();
	
	public static ConnectionManager getInstance() {
		return instance;
	}

	public void createNewConnection(Socket socket) {
		Connection conn = new Connection(socket);
		connections.add(conn);
		
		this.broadcast("Hello everyone, a newcomer has joined us.");
		System.out.println(this.connections.get(0).read());
	}
	
	public void broadcast(String str){
		for(Connection conn : connections){
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
