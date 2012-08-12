package com.golddigger.services;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;

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
	
	public MoveService(Map<String, Integer> costs){
		this();
		if (costs != null) customCosts.putAll(costs);
	}

	@Override
	public boolean runnable(String url) {
		String action = parseURL(url, URL_ACTION);
		String strDirection = parseURL(url, URL_EXTRA1);
		if (strDirection == null) return false;
		Direction direction = Direction.parse(strDirection);
		return action.equalsIgnoreCase(ACTION_TEXT);
	}

	public abstract boolean canMoveIn(Direction direction);

	@Override
	public boolean execute(String url, PrintWriter out) {
		Direction direction = Direction.parse(parseURL(url, URL_EXTRA1));
		if (!canMoveIn(direction)) {
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
			Point2D target = getOffset(unit.getPosition(), direction);
			tile = game.getMap().get(target);
			if (tile == null) {
				out.println("FAILED");
			} else if (!tile.isTreadable() || game.isUnitAt(target)){
				out.println("FAILED");
			} else {
				unit.setPosition(target);
				out.println("OK");
			}
		}
		
		try {
			Integer cost = customCosts.get(tile.toString());
			if (cost == null) {
				cost = tile.getDefaultMovementCost();
			}
			Thread.sleep(cost);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	public int getCost(String key) {
		return customCosts.get(key);
	}
	
	public Point2D getOffset(Point2D position, Direction direction){
		int x = 0, y = 0, i = x%2;
		switch(direction){
		case NORTH: return position.add(-1,0);
		case SOUTH: return position.add(1,0);
		case EAST: return position.add(0,1);
		case WEST: return position.add(0,-1);
		case NORTH_EAST: return position.add(1,i-1);
		case SOUTH_EAST: return position.add(1,i);
		case NORTH_WEST: return position.add(-1,i-1);
		case SOUTH_WEST: return position.add(-1,i);
		default: break;
		}
		return position.add(x,y);
	}
}