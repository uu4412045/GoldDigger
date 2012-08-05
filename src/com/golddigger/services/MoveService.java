package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;

public class MoveService extends Service {
	public static final String ACTION_TEXT = "move";

	public MoveService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		System.out.println("Executing Move Service");
		String direction = parseURL(url, URL_EXTRA1);
		if (direction == null){
			out.println("FAILED");
			return true;
		} 

		System.out.println("  => Parsing Direction");
		Point2D offset = parseDirection(direction);
		if (offset == null){
			out.println("FAILED");
			return true;
		}

		System.out.println("  => Getting Player");
		Player player = AppContext.getPlayer(parseURL(url, URL_PLAYER));
		if (player == null){
			out.println("FAILED");
			return true;
		}

		System.out.println("  => Getting Game");
		Game game = AppContext.getGame(player);
		if (game == null){
			out.println("FAILEDe");
			return true;
		}
		
		System.out.println("  => Getting Unit");
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("FAILED");
			return true;
		}
		
		synchronized (game){

			System.out.println("  => Checking to see if tile is treadable");
			int x = unit.getX()+offset.x, y = unit.getY() + offset.y;
			Tile tile = game.getMap().get(x, y);
			if (tile == null) {
				System.out.println("  ==> Tile is off the map");
				out.println("FAILED");
				return true;
			}
			else if (!tile.isTreadable()){
				System.out.println("  ==> Nope it isnt");
				out.println("FAILED");
				return true;
			} else {
				System.out.println("  ==> Yes it is");
				unit.setPosition(x, y);
				out.println("OK");
				return true;
			}
		}
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
	
	public static String createURL(String player, Direction d){
		return "/golddigger/digger/"+player+"/move/"+d.toString();
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
