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

public class MoveService extends Service {
	public static final String ACTION_TEXT = "move";
	/**
	 * Holds all the custom movement costs for this service.
	 */
	private HashMap<String, Integer> customCosts;
	
	/**
	 * offsets int the DEFAULT_COSTS array for this tile
	 */
	public static final int BASE_TILE = 0, CITY_TILE = 1, DEEP_WATER_TILE = 2,
		FOREST_TILE = 3, GOLD_TILE = 4, HILL_TILE = 5, MOUNTAIN_TILE = 6,
		ROAD_TILE = 7, SHALLOW_WATER_TILE = 8, TELEPORT_TILE = 9;
	/**
	 * Contains all the default movement costs for each tile type
	 */
	public static final int[] DEFAULT_TILE_COSTS = 
		{100,200,500,300,100,175, 500, 25,150, 100};
	
	private static final int DEFAULT_MOVEMENT_COST = 100;
	
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
			out.println("FAILED");
			return true;
		}

		Game game = AppContext.getGame(player);
		if (game == null){
			out.println("FAILEDe");
			return true;
		}
		
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("FAILED");
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
			long cost = getCost(tile);
			Thread.sleep(cost);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Used to determine the costs of moving to a particular tile type
	 * @param tile The tile that you want the cost for
	 * @return The time penalty in milliseconds
	 */
	private int getCost(Tile tile) {
		String key = tile.getClass().getSimpleName();
		if (tile instanceof GoldTile) {
			key += "_"+((GoldTile) tile).getGold();
		}
		if (customCosts.containsKey(key)) return customCosts.get(key);
		if (tile instanceof BaseTile) return DEFAULT_TILE_COSTS[BASE_TILE];
		if (tile instanceof CityTile) return DEFAULT_TILE_COSTS[CITY_TILE];
		if (tile instanceof DeepWaterTile) return DEFAULT_TILE_COSTS[DEEP_WATER_TILE];
		if (tile instanceof ForestTile) return DEFAULT_TILE_COSTS[FOREST_TILE];
		if (tile instanceof GoldTile) return DEFAULT_TILE_COSTS[GOLD_TILE];
		if (tile instanceof HillTile) return DEFAULT_TILE_COSTS[HILL_TILE];
		if (tile instanceof ShallowWaterTile) return DEFAULT_TILE_COSTS[SHALLOW_WATER_TILE];
		if (tile instanceof MountainTile) return DEFAULT_TILE_COSTS[MOUNTAIN_TILE];
		if (tile instanceof RoadTile) return DEFAULT_TILE_COSTS[ROAD_TILE];
		if (tile instanceof TeleportTile) return DEFAULT_TILE_COSTS[TELEPORT_TILE];
		return DEFAULT_MOVEMENT_COST;
	}

	@Override
	public boolean caresAboutConsumption() {
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
