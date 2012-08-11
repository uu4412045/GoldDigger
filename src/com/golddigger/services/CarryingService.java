package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.server.GameService;

/**
 * This service returns the amount of gold being carried by a {@link Player}'s {@link Unit}.
 * @author Brett Wandel
 * @see Unit
 * @see Player
 */
public class CarryingService extends GameService {
	public static final String ACTION_TEXT = "carrying";

	public CarryingService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		Player player = game.getPlayer(parseURL(url, URL_PLAYER));
		
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}
		
		out.println(""+unit.getGold());
		return true;
	}
	
}
