package com.golddigger.services;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.server.GameService;

public abstract class MoveService extends GameService {
	public static final String ACTION_TEXT = "move";
	
	/**
	 * Holds all the custom movement costs for this service.
	 */
	private HashMap<String, Integer> customCosts;

	public MoveService(){
		super(BASE_PRIORITY);
	}
	
	public MoveService(Map<String, Integer> costs){
		this();
		customCosts.putAll(costs);
	}

	@Override
	public boolean runnable(String url) {
		String action = parseURL(url, URL_ACTION);
		Direction direction = Direction.parse(parseURL(url, URL_EXTRA1));
		return action.equalsIgnoreCase(ACTION_TEXT) && canMoveIn(direction);
	}

	public abstract boolean canMoveIn(Direction direction);

	@Override
	public boolean execute(String url, PrintWriter out) {
		Direction direction = Direction.parse(parseURL(url, URL_EXTRA1));
		Player player = game.getPlayer(parseURL(url, URL_PLAYER));
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}
		
		Tile tile;
		synchronized (game){ //stop other units in the same game moving at the same time.
			Point2D offset = getOffset(unit.getPosition(), direction);
			Point2D target = unit.getPosition().add(offset);
			tile = game.getMap().get(target);
			if (tile == null) { // (x,y) is out of the map's boundary
				out.println("FAILED");
				return true;
			} else if (!tile.isTreadable()){
				out.println("FAILED");
				return true;
			} else if (game.isUnitAt(target)) {
				out.println("FAILED");
				return true;
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
		case NORTH: return position.add(0,-1);
		case SOUTH: return position.add(0,1);
		case EAST: return position.add(1,0);
		case WEST: return position.add(-1, 0);
		case NORTH_EAST: return position.add(1,i-1);
		case SOUTH_EAST: return position.add(1,i);
		case NORTH_WEST: return position.add(-1,i-1);
		case SOUTH_WEST: return position.add(-1,i);
		default: break;
		}
		return position.add(x,y);
	}
}