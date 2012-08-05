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

public class DropService extends Service {
	public static final String ACTION_TEXT = "drop";

	public DropService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		System.out.println("Executing Drop Service");

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

		System.out.println("  => Making sure the unit has gold to drop");
		if (unit.getGold() == 0){
			System.out.println("  <= Unit has no gold");
			out.println("0");
			return true;
		}

		System.out.println("  => Getting Unit");
		Tile tile = game.getMap().get(unit.getX(), unit.getY());
		if (tile == null){
			out.println("FAILED: Unit is out of bounds");
			return true;
		}

		synchronized (game){
			if (tile instanceof BaseTile){
				System.out.println("  => Base tile, adding gold to score");
				player.setScore(player.getScore() + unit.getGold());
				out.println(unit.getGold());
				unit.setGold(0);
			} else if (tile instanceof GoldTile) {
				System.out.println("  => Normal Gold Tile, dropping to the ground");
				GoldTile goldTile = (GoldTile) tile;
				int qty = unit.getGold();
				if (qty + goldTile.getGold() > 9) {
					qty = 9-goldTile.getGold();
				}
				goldTile.setGold(goldTile.getGold() + qty);
				unit.setGold(unit.getGold() - qty);
				out.println(""+qty);
			} else {
				out.println("FAILED: cant drop here");
			}
		}
		return true;
	}

	@Override
	public boolean caresAboutConsumption() {
		return true;
	}

}
