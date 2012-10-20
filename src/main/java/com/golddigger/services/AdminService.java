package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
/**
 * The AdminServer provides the basic functionality found in the orginial GoldDigger server.
 * It allows administrators to either list current players or add a new player.
 * @author Brett Wandel
 */
public class AdminService extends ServerService {
/* TODO: change these to non-static variables.
 * A good secure server should have a customisable name and password
 */ private static final String NAME = "admin", SECRET = "ccret";

	public AdminService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		String name = parseURL(url, URL_TARGET);
		return (name != null && name.equalsIgnoreCase(NAME));
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		String secret = parseURL(url, URL_PLAYER);
		String action = parseURL(url, URL_ACTION);

		if (secret == null || !secret.trim().equals(SECRET.trim()) || action == null) {
			out.println("bad command");
			return true;
		}

		if (action.equalsIgnoreCase("listdiggers")){
			String output = "";
			for (Player player : server.getPlayers()){
				output += player.getName() + " "+player.getSecret() +'\n';
			}
			out.println(output);
		}
		
		if (action.equalsIgnoreCase("add")){
			String name = parseURL(url, URL_EXTRA1);
			String password = parseURL(url, URL_EXTRA2);
			
			if (name == null || password == null){
				out.println("FAILED");
			} else if (server.getPlayer(password) != null){
				out.println("FAILED");
			} else {
				server.add(new Player(name, password));
				out.println("OK");
			}
		}
		return true;
	}

}
