package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;

public class GoldService extends GameService {

	private static final int MAX_UNIT_GOLD = 3;

	public GoldService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return Action.parse(parseURL(url, URL_ACTION)) != null;
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		//TODO: Add checks in
		Player player = game.getPlayer(parseURL(url, URL_PLAYER));
		Unit unit = game.getUnit(player);
		Action action = Action.parse(parseURL(url, URL_ACTION));
		Tile tile = game.getMap().get(unit.getX(), unit.getY());
		
		String result = "FAILED";
		switch (action){
		case CARRYING:
			result = unit.getGold()+""; break;
		case SCORE:
			result = player.getScore()+""; break;
		case GRAB:
			result = grab(unit, tile); break;
		case DROP:
			result = drop(unit, tile, player); break;
		}
		out.println(result);
		return false;
	}

	private enum Action {
		CARRYING, GRAB, DROP, SCORE;
		public static Action parse(String action){
			if (action.equalsIgnoreCase(GRAB.toString())) return GRAB;
			if (action.equalsIgnoreCase(CARRYING.toString())) return CARRYING;
			if (action.equalsIgnoreCase(DROP.toString())) return DROP;
			if (action.equalsIgnoreCase(SCORE.toString())) return SCORE;
			else return null;
		}
	}
	
	private String grab(Unit unit, Tile tile){
		if (unit.getGold() == MAX_UNIT_GOLD) return "FAILED";
		
		synchronized (game) {
			if (tile instanceof GoldTile){
				GoldTile goldTile = (GoldTile) tile;
				if (goldTile.getGold() == 0) return "FAILED";
				else {
					int qty = MAX_UNIT_GOLD - unit.getGold();
					if (qty > goldTile.getGold()){
						qty = goldTile.getGold();
					}

					goldTile.setGold(goldTile.getGold() - qty);
					unit.setGold(unit.getGold() + qty);
					return ""+qty;
				}
			} else return "FAILED";
		}
	}
	
	private String drop(Unit unit, Tile tile, Player player) {
		if (unit.getGold() == 0) return "FAILED";
		int qty;
		synchronized (game){
			if (tile instanceof BaseTile){
				if (((BaseTile) tile).getOwner() == player){
					player.setScore(player.getScore() + unit.getGold());
					qty = unit.getGold();
					unit.setGold(0);
				} else return "FAILED";
			} else if (tile instanceof GoldTile) {
				GoldTile goldTile = (GoldTile) tile;
				qty = unit.getGold();
				if (qty + goldTile.getGold() > 9) {
					qty = 9-goldTile.getGold();
				}
				goldTile.setGold(goldTile.getGold() + qty);
				unit.setGold(unit.getGold() - qty);
			} else return "FAILED";
			
			return qty+"";
		}
	}
}
