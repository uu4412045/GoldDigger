package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
/**
 * This service will progress the player on to the next map.
 * Will return: <br />
 * <ul>
 * 	<li>"FAILED" if there is still gold on the map</li>
 * 	<li>"FAILED" if the player's unit is still carrying gold</li>
 * </ul>
 * @author Brett Wandel
 * @see Player
 * @see Unit
 */
public class NextService extends Service {
	public static final String ACTION_TEXT = "next";
	public NextService(String contextID) {
		super(BASE_PRIORITY, contextID);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).contentEquals(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		Player player = AppContext.getContext(contextID).getPlayer(parseURL(url, URL_PLAYER));
		if (player == null){
			out.println("ERROR: Invalid Player Given");
			return true;
		}

		Game game = AppContext.getContext(contextID).getGame(player);
		if (game == null){
			out.println("ERROR: Player is currently not in a game");
			return true;
		}

		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}

		synchronized (game){
			if (unit.getGold() != 0){
				out.println("FAILED");
			} else if (game.getMap().hasGoldLeft()) {
				out.println("FAILED");
			} else {
				AppContext.getContext(contextID).progress(player);
				out.println("OK");
			}
		}
		return true;
	}

}
