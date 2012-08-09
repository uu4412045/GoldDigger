package com.golddigger.core.impl;

import com.golddigger.core.DelayedServer;
import com.golddigger.core.GoldDiggerServer;

/**
 * A simple {@link DelayedServer} that allows your to pass commands directy to it.
 * @author Brett
 *
 */
public class DirectInputDelayedServer extends DelayedServer {
	public DirectInputDelayedServer(GoldDiggerServer server, long delay) {
		super(server, delay);
	}

	private volatile String log = "";
	
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
	
	public synchronized void add(String line){
		log += line.trim()+"\n";
	}

}
