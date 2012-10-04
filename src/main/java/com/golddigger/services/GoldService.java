package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
/**
 * Gold Service contains all the basic commands for gold gathering.
 * It has grab, drop, carrying and score.
 * Should be added to every game.
 * 
 * @author Brett Wandel
 *
 */
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
		Tile tile = game.getMap().get(unit.getLat(), unit.getLng());
		
		String result = "FAILED";
		switch (action){
		case CARRYING:
			result = unit.getGold()+""; break;
		case SCORE:
			result = score(player, tile); break;
		case GRAB:
			result = grab(unit, tile); break;
		case DROP:
			result = drop(unit, tile, player); break;
		}
		out.println(result);
		return false;
	}

	private String score(Player player, Tile tile) {
		if (tile instanceof BaseTile){
			BaseTile base = (BaseTile) tile;
			Player owner = base.getOwner();
			//spy on another base
			if (owner != null && owner != player){
				return owner.getScore()+" "+owner.getName();
			}
		}
		
		return player.getScore()+"";
	}

	/**
	 * Simple enum used to easily determine what command is being executed.
	 * @author Brett Wandel
	 */
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
	
	/**
	 * perform "the grab" logic
	 * @param unit The unit grabbing the gold
	 * @param tile the tile the unit is on
	 * @return The number of gold picked up, or FAILED
	 */
	private String grab(Unit unit, Tile tile){
		if (unit.getGold() == MAX_UNIT_GOLD) return "0";
		
		synchronized (game) {
			if (tile instanceof GoldTile){
				GoldTile goldTile = (GoldTile) tile;
				if (goldTile.getGold() == 0) return "0";
				else {
					int qty = MAX_UNIT_GOLD - unit.getGold();
					if (qty > goldTile.getGold()){
						qty = goldTile.getGold();
					}

					goldTile.setGold(goldTile.getGold() - qty);
					unit.setGold(unit.getGold() + qty);
					return ""+qty;
				}
			} else return "0";
		}
	}
	
	/**
	 * perform the drop logic
	 * @param unit The unit dropping the gold
	 * @param tile The tile the gold is being dropped on.
	 * @param player The player who owns the unit.
	 * @return The number of gold dropped, or FAILED
	 */
	private String drop(Unit unit, Tile tile, Player player) {
		if (unit.getGold() == 0) return "0";
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
