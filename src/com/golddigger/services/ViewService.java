package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.*;
import com.golddigger.plugins.Plugin;
import com.golddigger.server.GameService;
/**
 * This service will return the view of the player's unit.
 * @author Brett Wandel
 * @see Player
 * @see Unit
 */
public class ViewService extends GameService {
	public static final String ACTION_TEXT = "view";
	public static final int DEFAULT_LINE_OF_SIGHT = 1;
	private int lineOfSight = 1;

	public ViewService(){
		this(DEFAULT_LINE_OF_SIGHT);
	}
	
	public ViewService(int lineOfSight) {
		super(BASE_PRIORITY);
		this.lineOfSight = lineOfSight;
	}
	
	public void setLineOfSight(int lineOfSight){
		this.lineOfSight = lineOfSight;
	}
	
	public int getLineOfSight(){
		return this.lineOfSight;
	}
	
	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		Player player = game.getPlayer(parseURL(url,URL_PLAYER));
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}

		Tile[][] area = game.getMap().getArea(unit.getX(), unit.getY(), lineOfSight);

		for (Tile[] row : area){
			for (Tile tile : row){
				out.append(convert(tile));
			}
			out.append('\n');
		}

		
		game.getPlugins(Plugin.class);
		return true;
	}
	
	/**
	 * Used to convert each tile to their respective character.
	 * @param t The tile to be converted
	 * @return A character representation of that tile
	 */
	public static char convert(Tile t){
		if (t == null) return ' ';
		if (t instanceof OccludedTile) return '?';
		if (t instanceof WallTile) return 'w';
		if (t instanceof BaseTile) return 'b';
		if (t instanceof CityTile) return 'c';
		if (t instanceof DeepWaterTile) return 'd';
		if (t instanceof ShallowWaterTile) return 's';
		if (t instanceof ForestTile) return 'f';
		if (t instanceof HillTile) return 'h';
		if (t instanceof RoadTile) return 'r';
		if (t instanceof TeleportTile) return 't';
		if (t instanceof GoldTile){
			switch (((GoldTile) t).getGold()){
			case 1: return '1';
			case 2: return '2';
			case 3: return '3';
			case 4: return '4';
			case 5: return '5';
			case 6: return '6';
			case 7: return '7';
			case 8: return '8';
			case 9: return '9';
			default: return '.';
			}
		}
		return '?';
	}

}
