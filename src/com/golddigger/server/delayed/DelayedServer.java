package com.golddigger.server.delayed;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.GoldDiggerServlet;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.utils.NullWriter;

public abstract class DelayedServer extends Thread  {
	private String contextID;
	private GoldDiggerServlet servlet;
	private long delay;
	private AppContext context;

	public DelayedServer(String contextId, long delay) {
		context = new AppContext(contextID);
		this.servlet = new GoldDiggerServlet(contextID);
		this.delay = delay;
	}

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
					process(parts[1]);
				} else {
					sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void process(String url){
		String name = Service.parseURL(url, Service.URL_PLAYER);
		Player player = AppContext.getContext(contextID).getPlayer(name);
		Game game = AppContext.getContext(contextID).getGame(player);
		servlet.process(url, new PrintWriter(NullWriter.INSTANCE), player, game);
	}

	public AppContext getContext(){
		return this.context;
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
