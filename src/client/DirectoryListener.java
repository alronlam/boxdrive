package client;

/*
 * Copyright (c) 2008, 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import job.CreateJob;
import job.DeleteJob;
import job.Job;
import job.JobManager;
import client.filerecords.ClientFileRecordManager;
import conn.Connection;

/**
 * Class for watching a directory (or tree) for changes to files. Modified from
 * https://docs.oracle.com/javase/tutorial/essential/io/notification.html
 */

public class DirectoryListener {
	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private final boolean recursive = true;
	private boolean trace = false;
	private Connection connection = null;
	private JobManager jobManager = null;

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	
	
	
	/**
	 * Register the given directory with the WatchService
	 */
	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		if (trace) {
			Path prev = keys.get(key);
			if (prev == null) {
				System.out.format("register: %s\n", dir);
			} else {
				if (!dir.equals(prev)) {
					System.out.format("update: %s -> %s\n", prev, dir);
				}
			}
		}
		keys.put(key, dir);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Creates a WatchService and registers the given directory
	 */
	DirectoryListener(Path path, Connection connection, JobManager jobManager) throws IOException {
		Path dir = path;
		this.connection = connection;
		this.jobManager = jobManager;
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();

		if (recursive) {
			System.out.format("Scanning %s ...\n", dir);
			registerAll(dir);
			System.out.println("Done.");
		} else {
			register(dir);
		}

		// enable trace after initial registration
		this.trace = true;
	}

	private void create(Path path) {
		if (connection != null) {
			Job createJob = new CreateJob(path, connection);
			jobManager.handleNewJob(createJob);
			try {
				ClientFileRecordManager.getInstance().handleCreateOrModify(path.getFileName().toString(),
						Files.getLastModifiedTime(path).toMillis());
			} catch (IOException e) {
				e.printStackTrace();
				// should do something here in case recording the create wasn't
				// successful
			}
		}
	}

	private void modify(Path path) {
		if (connection != null) {
			Job createJob = new CreateJob(path, connection);
			jobManager.handleNewJob(createJob);
			try {
				ClientFileRecordManager.getInstance().handleCreateOrModify(path.getFileName().toString(),
						Files.getLastModifiedTime(path).toMillis());
			} catch (IOException e) {
				e.printStackTrace();
				// should do something here in case recording the create wasn't
				// successful
			}
		}
	}

	private void delete(Path path) {
		if (connection != null) {
			Job deleteJob = new DeleteJob(path, System.currentTimeMillis(), connection);
			jobManager.handleNewJob(deleteJob);
			ClientFileRecordManager.getInstance().delete(path.getFileName().toString());
		}
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	void processEvents() {
		while (true) {
			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				Kind<?> kind = event.kind();

				// TBD - provide example of how OVERFLOW event is handled
				if (kind == OVERFLOW) {
					continue;
				}

				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = dir.resolve(name);

				// If directory is created, and watching recursively, then
				// register it and its sub-directories
				if (recursive && (kind == ENTRY_CREATE)) {
					try {
						if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
							registerAll(child);
						}
					} catch (IOException x) {

					}
				}

				if (kind == ENTRY_CREATE) {
					System.out.println("create called");
//					create(child);
					continue;

				} else if (kind == ENTRY_DELETE) {
					delete(child);
					continue;

				} else if (kind == ENTRY_MODIFY) {
					System.out.println("modify called");
//					modify(child);
					continue;
				}
			}

			// Reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// All directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}
}
