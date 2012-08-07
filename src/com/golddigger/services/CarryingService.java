package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;

/**
 * This service returns the amount of gold being carried by a {@link Player}'s {@link Unit}.
 * @author Brett Wandel
 * @see Unit
 * @see Player
 */
public class CarryingService extends Service {
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
		AppContext context = getContextFromURL(url);
		Player player = context.getPlayer(parseURL(url, URL_PLAYER));
		if (player == null){
			out.println("ERROR: Invalid Player Given");
			return true;
		}

		Game game = context.getGame(player);
		if (game == null){
			out.println("ERROR: Player is currently not in a game");
			return true;
		}
		
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}
		
		out.println(""+unit.getGold());
		return true;
	}
	
}
