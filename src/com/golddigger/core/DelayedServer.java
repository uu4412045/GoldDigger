package com.golddigger.core;

import java.io.PrintWriter;

import com.golddigger.core.GoldDiggerServer;
import com.golddigger.utils.NullWriter;
/**
 * The Base for a Delayed server. used to provide a time lag spectator view for the competitors
 * @author Brett Wandel
 */
public abstract class DelayedServer extends Thread  {
	private GoldDiggerServer server;
	private long delay;
	private PrintWriter	devNull = new PrintWriter(NullWriter.INSTANCE);

	/**
	 * Create a new DelayedService
	 * @param server the server to be used
	 * @param delay the time delay in milliseconds
	 */
	public DelayedServer(GoldDiggerServer server, long delay) {
		this.delay = delay;
		this.server = server;
	}

	/**
	 * get the next command in the log
	 * @return the next command
	 */
	protected abstract String next();

	@Override
	public void run(){
		try {
			while(true){
				String line;
				if ((line = next()) != null) {
					line = line.trim();
					String[] parts = line.split(",");
					long current, time = Long.parseLong(parts[0]);
					while (time+delay > (current = System.currentTimeMillis())){
						Thread.sleep((time + delay) - current+100);
					}
					server.process(parts[1], devNull);
				} else {
					sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Build a valid entry for the delay server to read.
	 * @param url The url that has come in
	 * @return Valid log entry to be added to the DelayedServer
	 */
	public static String buildEntry(String url){
		if (url.contains(",")) throw new RuntimeException("the url can NOT contain a comma(\",\")");
		return System.currentTimeMillis() + ","+ url;
	}
}
