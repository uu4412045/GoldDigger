package com.golddigger.server.impl;

import com.golddigger.server.GoldDiggerServer;


/**
 * A simple {@link DelayedServer} that allows your to pass commands directly to it.
 * @author Brett Wandel
 *
 */
public class DirectInputDelayedServer extends DelayedServer {
	public DirectInputDelayedServer(GoldDiggerServer server, long delay) {
		super(server, delay);
	}

	private String log = "";
	
	@Override
	protected synchronized String next() {
		if (log.contains("\n")){
			int i = log.indexOf('\n');
			String line = log.substring(0, i);
			log = log.substring(i+1);
			return line.trim();
		} else {
			return null;
		}
	}
	
	/**
	 * Add a log entry to the server
	 * @param line the log entry to add
	 */
	public synchronized void add(String url){
		log += buildEntry(url);
	}

}
