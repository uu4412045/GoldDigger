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
		synchronized (game){
			int x = unit.getX()+offset.x, y = unit.getY() + offset.y;
			tile = game.getMap().get(x, y);
			if (tile == null) {
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

	private int getCost(Tile tile) {
		String key = tile.getClass().getSimpleName();
		if (customCosts.containsKey(key)) return customCosts.get(key);
		if (tile instanceof BaseTile) return 100;
		if (tile instanceof CityTile) return 200;
		if (tile instanceof DeepWaterTile) return 500;
		if (tile instanceof ForestTile) return 300;
		if (tile instanceof GoldTile) return 100;
		if (tile instanceof HillTile) return 175;
		if (tile instanceof ShallowWaterTile) return 150;
		if (tile instanceof MountainTile) return 500;
		if (tile instanceof RoadTile) return 25;
		if (tile instanceof TeleportTile) return 100;
		return 100;
	}

	@Override
	public boolean caresAboutConsumption() {
		return true;
	}
	
	private Point2D parseDirection(String direction){
		if (direction.equalsIgnoreCase("north")) return new Point2D(-1,0);
		if (direction.equalsIgnoreCase("south")) return new Point2D(1,0);
		if (direction.equalsIgnoreCase("east"))  return new Point2D(0,1);
		if (direction.equalsIgnoreCase("west"))  return new Point2D(0,-1);
		return null;
	}
	
	public enum Direction{
		NORTH,SOUTH,EAST,WEST;
		
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
