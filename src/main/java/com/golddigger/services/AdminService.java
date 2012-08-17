package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;

public class AdminService extends ServerService {
	private static final String NAME = "admin", SECRET = "ccret";


	public AdminService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		String name = parseURL(url, URL_TARGET);
		if (name == null) return false;
		return name.equalsIgnoreCase(NAME);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		String secret = parseURL(url, URL_PLAYER);
		String action = parseURL(url, URL_ACTION);

		if (secret == null || !secret.equals(SECRET) || action == null) {
			out.println("bad command");
			return true;
		}

		if (action.equalsIgnoreCase("listdiggers")){
			for (Player player : server.getPlayers()){
				out.println(player.getName() + " "+player.getSecret());
			}
		} else if (action.equalsIgnoreCase("add")){
			String name = parseURL(url, URL_EXTRA1);
			String password = parseURL(url, URL_EXTRA2);
			
			if (name == null || password == null){
				out.println("FAILED");
			} else if (server.getPlayer(name) != null){
				out.println("FAILED");
			} else {
				server.add(new Player(name, password));
				out.println("OK");
			}
		}
		return true;
	}

}
