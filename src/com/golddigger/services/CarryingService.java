package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;

public class CarryingService extends Service {
	public static final String ACTION_TEXT = "carrying";

	public CarryingService() {
		super(BASE_PRIORITY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		System.out.println("Executing Carrying Service");

		System.out.println("  => Getting Player");
		Player player = AppContext.getPlayer(parseURL(url, URL_PLAYER));
		if (player == null){
			out.println("FAILED: Invalid Player Given");
			return true;
		}

		System.out.println("  => Getting Game");
		Game game = AppContext.getGame(player);
		if (game == null){
			out.println("FAILED: Player is currently not in a game");
			return true;
		}
		
		System.out.println("  => Getting Unit");
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("FAILED: no unit found for this player");
			return true;
		}
		
		out.println(""+unit.getGold());
		return true;
	}

	@Override
	public boolean caresAboutConsumption() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
