package serverjobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import server_manager.FileDirectory;
import server_manager.FileObject;
import job.BasicJob;
import job.Job;
import job.JobManager;
import conn.Connection;
import conn.ConnectionManager;

public class CoordinatorJobManager extends JobManager{
	private FileDirectory fileDirectory;
	private Map<Connection,List<Job>> lastProcessedJobs;
	private static int HISTORY_SIZE =10;
	
	public CoordinatorJobManager(ConnectionManager connMgrClients,ConnectionManager connMgrStorageServers) {
		super(connMgrClients,connMgrStorageServers);
		
		lastProcessedJobs = new HashMap<>();
	}
	
	public CoordinatorJobManager() {
		// TODO Auto-generated constructor stub
	}

	public void updateFileDirectory(){
		Connection conn = connMgrStorageServers.getLastConnection();
		if(fileDirectory.addServer(conn))
			lastProcessedJobs.put(conn, new ArrayList<Job>());
	}
	
	public String forwardJob(Job job){
		return null;
	}

	@Override
	protected synchronized void processMessages() {
		while (jobQueue.size() > 0) {
			Job job = this.dequeue(0);
			
			if(connMgrClients.hasConnection(job.getConnection())){
				// the job came from clients
				
				// get serverlist from fileDirectory
				List<Connection> connList = fileDirectory.getServerListForFile(((BasicJob)job).file);
				
				// send to storage server group
				for(Connection conn : connList)
					conn.write(job.getJson());
			}
			else {
				// the job came from storage servers
				
				if(isBroadcastJob(job)){
					connMgrClients.broadcast(job.getJson());
				}
				else{
					addJobToHistory(job);
					// send to specific client
				}
			}
		}
	}
	
	private boolean isBroadcastJob(Job job){
		List<Job> list = lastProcessedJobs.get(job.getConnection());
		return list.contains(job);
	}
	
	private void removeJobFromHistory(Job job){
		List<Job> list = lastProcessedJobs.get(job.getConnection());
		list.remove(job);
	}
	
	private void addJobToHistory(Job job){
		List<Job> list = lastProcessedJobs.get(job.getConnection());
		list.add(job);
		while(list.size() > this.HISTORY_SIZE)
			list.remove(0);
	}
}
