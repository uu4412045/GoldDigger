package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Unit;

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
		System.out.println("Executing Next Service");

		System.out.println("  => Getting Player");
		Player player = AppContext.getPlayer(parseURL(url, URL_PLAYER));
		if (player == null){
			out.println("FAILED");
			return true;
		}

		System.out.println("  => Getting Game");
		Game game = AppContext.getGame(player);
		if (game == null){
			out.println("FAILED");
			return true;
		}

		System.out.println("  => Getting Unit");
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("FAILED");
			return true;
		}

		synchronized (game){
			if (unit.getGold() != 0){
				out.println("FAILED");
			} else if (game.getMap().hasGoldLeft()) {
				out.println("FAILED");
			} else {
				AppContext.progress(player);
				out.println("OK");
			}
		}
		return true;
	}

	@Override
	public boolean caresAboutConsumption() {
		return true;
	}

}
