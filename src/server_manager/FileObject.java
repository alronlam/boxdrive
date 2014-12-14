package server_manager;

import java.nio.file.Path;

import filemanager.FileBean;

public class FileObject {
	public FileBean file;
	public int config;
	
	public FileObject(FileBean file, int c) {
		this.file = file;
		config = c;
	}
	
	// lel
}
