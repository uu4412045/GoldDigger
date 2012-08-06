package com.golddigger.services;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.*;
/**
 * This service will move the {@link Player}'s {@link Unit} in a particular direction. <br />
 * Each tile has a movement cost which will delay the servers response to simulate different terrain.<br />
 * Will return: <br />
 * <ul>
 * 	<li>"FAILED" if the direction is invalid</li>
 *  <li>"FAILED" if the {@link Tile} is not "treadable"</li>
 *  <li>"FAILED" if the position is out of the maps boundaries.</li>
 * </ul>
 * @author Brett Wandel
 * @see Player
 * @see Unit
 */
public class MoveService extends Service {
	public static final String ACTION_TEXT = "move";
	/**
	 * Holds all the custom movement costs for this service.
	 */
	private HashMap<String, Integer> customCosts;
	
	public MoveService(Map<String, Integer> costs){
		this();
		customCosts.putAll(costs);
	}
	
	public MoveService() {
		super(BASE_PRIORITY);
		customCosts = new HashMap<String, Integer>();
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		String direction = parseURL(url, URL_EXTRA1);
		if (direction == null){
			out.println("FAILED");
			return true;
		} 

		Point2D offset = parseDirection(direction);
		if (offset == null){
			out.println("FAILED");
			return true;
		}

		Player player = AppContext.getPlayer(parseURL(url, URL_PLAYER));
		if (player == null){
			out.println("ERROR: Invalid Player Given");
			return true;
		}

		Game game = AppContext.getGame(player);
		if (game == null){
			out.println("ERROR: Player is currently not in a game");
			return true;
		}

		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}
		
		Tile tile;
		synchronized (game){ //stop other units in the same game moving at the same time.
			int x = unit.getX()+offset.x, y = unit.getY() + offset.y;
			tile = game.getMap().get(x, y);
			if (tile == null) { // (x,y) is out of the map's boundary
				out.println("FAILED");
				return true;
			}
			else if (!tile.isTreadable()){
				out.println("FAILED");
				return true;
			} else {
				unit.setPosition(x, y);
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
	
	/**
	 * Used to create an offset in a particular direction.
	 * @param direction The string representation
	 * @return The Point2D offset to be ADDED to the current location.
	 */
	private Point2D parseDirection(String direction){
		if (direction.equalsIgnoreCase("north")) return new Point2D(-1,0);
		if (direction.equalsIgnoreCase("south")) return new Point2D(1,0);
		if (direction.equalsIgnoreCase("east"))  return new Point2D(0,1);
		if (direction.equalsIgnoreCase("west"))  return new Point2D(0,-1);
		return null;
	}
	
	/**
	 * Used to define the string representations of each direction.
	 * @author Brett Wandel
	 */
	public enum Direction{
		NORTH,SOUTH,EAST,WEST;
		
		/**
		 * Returns the string representation of a particular direction.
		 */
		@Override
		public String toString(){
			switch(this){
			case NORTH: return "north";
			case SOUTH: return "south";
			case EAST: return "east";
			case WEST: return "west";
			default: return null;
			}
		}
	}
	
}
