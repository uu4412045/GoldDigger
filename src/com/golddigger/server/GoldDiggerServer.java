package com.golddigger.server;

import java.io.PrintWriter;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.services.Service;
/**
 * The GoldDiggerServer is the gateway between the commands from the competitors and the Game objects themselves.
 * @author Brett Wandel
 */
public abstract class GoldDiggerServer extends GameServer{
	/**
	 * Checks to make sure that the Player in the url exists, and that they are in a game.
	 * After the checks, the url is passed to the games services.
	 * @param url The "command" from the competitors
	 * @param out The output to the competitor
	 */
	public void process(String url, PrintWriter out){
		String name = Service.parseURL(url, Service.URL_PLAYER);
		Player player;
		Game game;
		
		if (name == null) {
			System.err.println("[GoldDiggerServer.java] Could not get a player from the url: "+url);
			return;
		} else if ((player = this.getPlayer(name)) == null){
			System.err.println("[GoldDiggerServer.java] No player by the name:"+name+". The URL:"+url);
			return;
		} else if ((game = this.getGame(player)) == null){
			System.err.println("[GoldDiggerServer.java] No game for player: "+name);
			return;
		}
		
		synchronized (this) {
			Service[] services = this.getServices();
			boolean consumed = false;
			for (Service service : services){
				if (service.caresAboutConsumption() && consumed){
					break; //skip this service
				} else if (service.runnable(url)){
					consumed = service.execute(url, out);
				}
			}
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
