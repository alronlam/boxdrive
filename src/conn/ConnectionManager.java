package conn;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import job.manager.JobManager;

public class ConnectionManager {
	//private static ConnectionManager connectionManager;

	private List<Connection> connections;

//	private static ConnectionManager getInstance(){
//		if(connectionManager == null)
//			connectionManager = new ConnectionManager();
//		return connectionManager;
//	}
	
	public ConnectionManager() {
		connections = new ArrayList<>();
	}

	public Connection createNewConnection(Socket socket, JobManager jobManager) {
		Connection conn = new Connection(socket, jobManager);
		connections.add(conn);
		return conn;
		// System.out.println("New connection with: " + socket.getInetAddress());
		// broadcast("Hello");
		// System.out.println(this.connections.get(0).read());
	}
	
	public Connection getConnection(int index){
		return connections.get(index);
	}
	
	public boolean hasConnection(Connection conn){
		return connections.contains(conn);
	}
	
	public Connection getLastConnection(){
		if(connections.size() == 0)
			return null;
		return connections.get(connections.size()-1);
	}
	
	public Socket getSocket(int index) {
		if (connections.size() > index)
			return connections.get(index).socket;
		else
			return null;
	}

	public void broadcast(String str) {
		for (Connection conn : connections) {
			conn.write(str);
		}
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

	// TODO: remove connection
}
