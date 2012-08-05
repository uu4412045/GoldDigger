package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;

public class NextService extends Service {
	public static final String ACTION_TEXT = "next";
	public NextService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).contentEquals(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		String name = parseURL(url, URL_PLAYER);
		Game game = AppContext.getGameByPlayer(name);
		if (game.getMap().hasGoldLeft()) {
			out.println("FAILED: Gold Still Available");
		} else {
			Player player = AppContext.getPlayer(name);
			AppContext.progress(player);
		}
		return true;
	}

	@Override
	public boolean caresAboutConsumption() {
		return true;
	}

}
