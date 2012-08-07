package com.golddigger.server.delayed;

/**
 * A simple {@link DelayedServer} that allows your to pass commands directy to it.
 * @author Brett
 *
 */
public class DirectInputDelayedServer extends DelayedServer {
	public DirectInputDelayedServer(String contextId, long delay) {
		super(contextId, delay);
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