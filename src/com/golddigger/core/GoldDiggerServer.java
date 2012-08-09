package com.golddigger.core;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;

public abstract class GoldDiggerServer {
	public void process(String url, PrintWriter out){
		AppContext context = Service.getContextFromURL(url);
		String name = Service.parseURL(url, Service.URL_PLAYER);
		Player player;
		Game game;
		
		if (context == null) {
			System.err.println("[GoldDiggerServer.java] Could not get a context from the URL: "+url);
			return;
		} else if (name == null) {
			System.err.println("[GoldDiggerServer.java] Could not get a player from the url: "+url);
			return;
		} else if ((player = context.getPlayer(name)) == null){
			System.err.println("[GoldDiggerServer.java] No player by that name: "+url);
			return;
		} else if ((game = context.getGame(player)) == null){
			System.err.println("[GoldDiggerServer.java] No game for player: "+name);
			return;
		}
		
		/*
		 * This block decides what services are run, as well as stopping
		 * people running parallel requests.
		 */
		synchronized (player) {
			Service[] services = game.getServices();
			boolean consumed = false;
			for (Service service : services){
				if (service.caresAboutConsumption() && consumed){
					break; //skip this service
				} else if (service.runnable(url)){
					consumed = service.execute(url, out);
				}
			}
		}
	}
}