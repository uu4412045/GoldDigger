package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.plugins.Plugin;
import com.golddigger.utils.MapMaker;
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

		Tile[][] area = game.getMap().getArea(unit.getLat(), unit.getLng(), lineOfSight);

		for (Tile[] row : area){
			for (Tile tile : row){
				out.append(MapMaker.convert(tile));
			}
			out.append('\n');
		}

		
		game.getPlugins(Plugin.class);
		return true;
	}

}
