package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.utils.JsonEncoder;
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
		String result = formatView(area, player, extra1);
		if (result == null) result = toChars(area);
		
		out.println(result);
		return true;
	}
	
	/**
	 * Converts an area and the included units into JSON for multiplayer view
	 * @param area The area to convert
	 * @param player The player who called the command.
	 * @return The view
	 */
	private String toJson(Tile[][] area, Player player){
		return JsonEncoder.encode(game, area, player);
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
	
	/**
	 * <p>Convert an area into a view of a particular "format".</p>
	 * <p>This function is provided for overriding by the {@link HexViewService}
	 * (mainly for human readable hex format).</p>
	 * 
	 * @param area The area to be converted
	 * @param format the requested "format"
	 * @return The output for the user
	 */
	public String formatView(Tile[][] area, Player player, String format){
		if (format != null){
			if (format.equalsIgnoreCase("json")){
				return toJson(area, player);
			}
		}
		return toChars(area);
	}

}
