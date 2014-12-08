package job;

import java.net.Socket;
import java.util.List;

public class ListJob extends Job {
	
	ListJob(Socket socket) {
		super(socket);
	}

	private List<FileBean> files;
	
	@Override
	public void execute() {
		
	}

}
