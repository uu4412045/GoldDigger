package com.golddigger.services;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;

/**
 * Movement Service contains all the core logic about how unit can and can not move.
 * It needs to be extended to determine which directions are valid movements.
 * 
 * @author Brett Wandel
 */
public abstract class MoveService extends GameService {
	public static final String ACTION_TEXT = "move";
	
	/**
	 * Holds all the custom movement costs for this service.
	 */
	private HashMap<String, Integer> customCosts;

	public MoveService(){
		super(BASE_PRIORITY);
		customCosts = new HashMap<String,Integer>();
	}
	
	/**
	 * Create a Movement service with custom costs.
	 * @param costs
	 */
	public MoveService(Map<String, Integer> costs){
		this();
		if (costs != null) customCosts.putAll(costs);
	}

	@Override
	public boolean runnable(String url) {
		String action = parseURL(url, URL_ACTION);
		if (!action.equalsIgnoreCase(ACTION_TEXT)) return false;
		
		String strDirection = parseURL(url, URL_EXTRA1);
		if (strDirection == null) return false;
		Direction direction = Direction.parse(strDirection);
		return direction != null;
	}

	/**
	 * Determines if a move is valid depending on which kind of tile the map has.
	 * @param direction
	 * @return
	 */
	public abstract boolean isValidDirection(Direction direction);

	@Override
	public boolean execute(String url, PrintWriter out) {
		Direction direction = Direction.parse(parseURL(url, URL_EXTRA1));
		if (!isValidDirection(direction)) {
			out.println("FAILED");
			return true;
		}
		
		Player player = game.getPlayer(parseURL(url, URL_PLAYER));
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}
		Tile tile;
		synchronized (game){ //stop other units in the same game moving at the same time.
			Point2D target = direction.getOffset(unit.getPosition());
			tile = game.getMap().get(target);
			if (tile == null) {
				out.println("FAILED");
				return true;
			} else if (!tile.isTreadable() || game.isUnitAt(target)){
				out.println("FAILED");
			} else {
				unit.setPosition(target);
				out.println("OK");
			}
		}

		try {
			Integer cost = this.getCost(tile);//customCosts.get(tile.toString());
			if (cost == null) {
				cost = tile.getDefaultMovementCost();
			}
			Thread.sleep(cost);;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Integer getCost(String key) {
		if (customCosts == null) return null;
		return this.customCosts.get(key);
	}
	
	public Integer getCost(Tile tile){
		return this.getCost(tile.toString());
	}
	
}