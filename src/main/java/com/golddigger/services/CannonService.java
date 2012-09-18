package com.golddigger.services;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;

/**
 * The Cannon Service provides the diggers with a cannon in which they can buy ammo
 * at their base, and shoot other diggers. Diggers that are shot drop their gold (if on a gold tile)
 * and respawn back at their base.
 * 
 * @author Brett Wandel
 *
 */
public class CannonService extends GameService {
	public static final int COST = 1;
	public static final int RANGE = 2;
	private Map<Player, Integer> ammo = new HashMap<Player, Integer>();

	public CannonService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return (parseURL(url, URL_ACTION).equalsIgnoreCase("cannon"));
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		String extra = parseURL(url, URL_EXTRA1);
		String name = parseURL(url , URL_PLAYER);
		Player player = game.getPlayer(name);
		Unit unit = game.getUnit(player);
		Point2D pos = unit.getPosition();

		if (extra.equalsIgnoreCase("buy")){
			if (player.getScore() < COST){
				out.println("FAILED: Dont have enough cash");
			} else if (game.getMap().get(pos) instanceof BaseTile){
				BaseTile base = (BaseTile) game.getMap().get(pos);
				
				if (base.getOwner() != player){
					out.println("FAILED: Not on your bank");
					return true;
				}
				
				player.setScore(player.getScore()-COST);
				Integer qty = ammo.get(player);
				if (qty == null) qty = 0;
				
				ammo.put(player, qty+1);
				out.println("OK you have "+(qty+1)+" rounds left");
			} else {
				out.println("FAILED: Not on your bank");
			}
			return true;
		} else if (extra.equalsIgnoreCase("shoot")){
			int lat, lng;
			try {
				lat = Integer.parseInt(parseURL(url, URL_EXTRA2));
				lng = Integer.parseInt(parseURL(url, URL_EXTRA3));
			} catch (NumberFormatException e){
				out.println("FAILED: invalid target");
				return true;
			}
			
			Point2D target = pos.add(lat, lng);
			double range = Math.sqrt(lat^2 + lng^2);
			if (range > RANGE){
				out.println("FAILED: out of range");
				return true;
			}
			
			Integer a = ammo.get(player);
			if (a == null || a <= 0) {
				out.println("FAILED: out of ammo");
				return true;
			} else {
				ammo.put(player, a-1);

				if (shoot(target)){
					out.println("HIT");
				} else {
					out.println("MISSED");
				}
			}
			
		}
		return true;
	}
	
	/**
	 * Shoot at the target position
	 * @param target the position to shoot at
	 * @return true if you hit a unit, false otherwise
	 */
	private boolean shoot(Point2D target){
		for (Unit unit : game.getUnits()){
			Point2D pos = unit.getPosition();
			if (pos.equals(target)){
				respawn(unit);
				int qty = unit.getGold();
				if (qty > 0){
					unit.setGold(0);
					Tile tile = game.getMap().get(target);
					if (tile instanceof GoldTile){
						GoldTile goldTile = (GoldTile) tile;
						qty += goldTile.getGold();
						if (qty > 9) qty = 9;
						goldTile.setGold(qty);
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Moves the unit back to their base.
	 * @param unit the unit to respawn
	 */
	private void respawn(Unit unit){
		Player target_owner = unit.getOwner();
		for (BaseTile base : game.getBases()){
			if (base.getOwner().equals(target_owner)){
				unit.setPosition(game.getMap().getPosition(base));
			}
		}
	}
}
