package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;

/**
 * This service will drop as much gold as it can from the {@link Player}'s {@link Unit}. <br \>
 * Will return: <br \>
 * <ul>
 * 	<li>"FAILED" if the unit has no gold</li>
 *  <li>"FAILED" if the Tile can not hold any more gold</li>
 *  <li>The amount of gold dropped.</li>
 * </ul>
 * @author Brett Wandel
 * @see Player
 * @see Unit
 */
public class DropService extends Service {
	public static final String ACTION_TEXT = "drop";

	public DropService(String contextID) {
		super(BASE_PRIORITY, contextID);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
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

		if (unit.getGold() == 0){
			System.out.println("FAILED");
			out.println("0");
			return true;
		}

		Tile tile = game.getMap().get(unit.getX(), unit.getY());
		if (tile == null){
			out.println("ERROR: Unit is out of bounds");
			return true;
		}

		synchronized (game){
			if (tile instanceof BaseTile){
				player.setScore(player.getScore() + unit.getGold());
				out.println(unit.getGold());
				unit.setGold(0);
			} else if (tile instanceof GoldTile) {
				GoldTile goldTile = (GoldTile) tile;
				int qty = unit.getGold();
				if (qty + goldTile.getGold() > 9) {
					qty = 9-goldTile.getGold();
				}
				goldTile.setGold(goldTile.getGold() + qty);
				unit.setGold(unit.getGold() - qty);
				out.println(""+qty);
			} else {
				out.println("FAILED");
			}
		}
		return true;
	}
}
