package com.golddigger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import com.golddigger.core.AppContext;
import com.golddigger.core.GoldDiggerServlet;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.utils.NullWriter;

public class DelayedServer extends Thread  {
	private String contextID;
	private GoldDiggerServlet servlet;
	private String log = "";
	private long delay;
	private AppContext context;

	public DelayedServer(String contextId, long delay) {
		context = new AppContext(contextID);
		this.servlet = new GoldDiggerServlet(contextID);
		this.delay = delay;
	}

	@Override
	public void run(){
		try {
			while(true){
				System.out.println("check");
				String[] lines = log.split("\n");
				if (lines.length == 0 || lines[0].equalsIgnoreCase("")) {
					System.out.println("sleep");
					sleep(100);
					continue;
				} else {
					System.out.println("found:"+lines[0]);
					String[] parts = lines[0].split(",");
					long time = Long.parseLong(parts[0]);
					long current = System.currentTimeMillis();
					if (time+delay > current){
						Thread.sleep(time + delay - current+10);
					}
					process(parts[1]);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void add(String contents){
		this.log += contents;
	}

	private void process(String url){
		System.out.println("[DelayedServer] parsing:"+url);
		String name = Service.parseURL(url, Service.URL_PLAYER);
		Player player = AppContext.getContext(contextID).getPlayer(name);
		Game game = AppContext.getContext(contextID).getGame(player);

		servlet.process(url, new PrintWriter(NullWriter.INSTANCE), player, game);
	}

	public AppContext getContext(){
		return this.context;
	}
}
