package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.GoldTile;

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
		System.out.println("Executing Grab Service");
		
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

		System.out.println("  => Getting Tile");
		Tile tile = game.getMap().get(unit.getX(), unit.getY());
		if (tile == null){
			out.println("FAILED: unit is out of bounds");
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
					out.println("FAILED: no gold on this tile");
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
				out.println("FAILED: no gold on this tile");
			}
		}
		return true;
	}

	@Override
	public boolean caresAboutConsumption() {
		return true;
	}

}
