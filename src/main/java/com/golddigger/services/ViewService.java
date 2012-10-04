package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
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

		Tile[][] area = this.getArea(unit);

		String extra1 = parseURL(url, URL_EXTRA1);
		String result;
		if (extra1 == null){
			result = toChars(area);
		} else { //TODO: should everything else be treated as to char?
			result = toChars(area);
		}
		out.println(result);
		return true;
	}
	
	/**
	 * Converts an area of tiles into characters which represent the view.
	 * @param area The area to convert
	 * @return The View
	 */
	private String toChars(Tile[][] area){
		String result = "";
		for (Tile[] row : area){
			for (Tile tile : row){
				result += MapMaker.convert(tile);
			}
			result += '\n';
		}
		
		return result;
	}
	
	public Tile[][] getArea(Unit unit){
		return game.getMap().getArea(unit.getLat(), unit.getLng(), lineOfSight);
	}

}
