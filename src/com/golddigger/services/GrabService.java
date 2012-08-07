package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.GoldTile;
/**
 * This service will grab as much gold as it can from the {@link Unit}'s location. <br \>
 * Will return: <br \>
 * <ul>
 * 	<li>"FAILED" if the unit can't hold any more gold</li>
 *  <li>"FAILED" if the tile has no gold<li>
 *  <li>"FAILED" if the tile is not a {@link GoldTile}
 *  <li>The amount of gold picked up.
 * </ul>
 * @author Brett Wandel
 * @see Player
 * @see Unit
 * @see GoldTile
 */
public class GrabService extends Service {
	public static final String ACTION_TEXT = "grab";
	public static final int MAX_UNIT_GOLD = 3;
	
	public GrabService() {
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

		Tile tile = game.getMap().get(unit.getX(), unit.getY());
		if (tile == null){
			out.println("ERROR: unit is out of bounds");
			return true;
		}

		if (unit.getGold() == MAX_UNIT_GOLD){
			out.println("FAILED");
			return true;
		}

		synchronized (game){
			if (tile instanceof GoldTile){
				GoldTile goldTile = (GoldTile) tile;
				if (goldTile.getGold() == 0){
					out.println("FAILED");
				} else {

					int qty = MAX_UNIT_GOLD - unit.getGold();
					if (qty > goldTile.getGold()){
						qty = goldTile.getGold();
					}

					goldTile.setGold(goldTile.getGold() - qty);
					unit.setGold(unit.getGold() + qty);
					out.println(""+qty);
				}
			} else {
				out.println("FAILED");
			}
		}
		return true;
	}
}
