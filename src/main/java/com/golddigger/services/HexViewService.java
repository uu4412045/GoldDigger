package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.utils.MapMaker;

public class HexViewService extends GameService {
	public static final String ACTION_TEXT = "view";
	public static final int DEFAULT_LINE_OF_SIGHT = 1;
	private static int TRUE=1, CHECK=2, CHECKED=3;
	private static final char HIDDEN_TILE_SYMBOL = '?';
	private int lineOfSight;

	public HexViewService() {
		this(DEFAULT_LINE_OF_SIGHT);
	}
	
	public HexViewService(int lineOfSight){
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
		Player player = game.getPlayer(parseURL(url, URL_PLAYER));
		
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("ERROR: no unit found for this player");
			return true;
		}

		Tile[][] area = game.getMap().getArea(unit.getLat(), unit.getLng(), lineOfSight);
		
		
		int[][] hex = mask((area.length-1)/2, unit);
		
		for (int lat = 0; lat < area.length; lat++) {
			for (int lng = 0; lng < area[lat].length; lng++) {
				if (hex[lat][lng] > 0) {
					out.append(MapMaker.convert(area[lat][lng]));
				} else {
					if (MapMaker.convert(area[lat][lng]) == '-') {
						out.append('-');
					} else {
						out.append(HIDDEN_TILE_SYMBOL);
					}
				}
			}
			out.append('\n');
		}
		
		return true;
	}
	
	/**
	 * need to rewrite for variable LOS
	 * @param area
	 * @return
	 */
	public static int[][] mask(int radius, Unit unit){
		int size = (radius*2)+1;
		int[][] mask = new int[size][size];
		mask[radius][radius] = CHECK;

		for (int i = 0; i < radius; i++){
			for (int lat = 0; lat < size; lat++){
				for (int lng = 0; lng < size; lng++) {
					if (mask[lat][lng] == CHECK) mask = markNeighbours(mask,lat,lng,unit);
				}
			}
			
			for (int lat = 0; lat < mask.length; lat++) {
				for (int lng = 0; lng < mask[lat].length; lng++) {
					if (mask[lat][lng] == TRUE) {
						mask[lat][lng] = CHECK;
					}
				}				
			}
		}

		return mask;
	}
	
	private static int[][] markNeighbours(int[][] area, int lat, int lng, Unit unit){
		int ref_lng = unit.getLng() + lng;
		if (area.length == 3) ref_lng = unit.getLng();
		
		area[lat][lng] = CHECKED;
		if (area[lat+1][lng] == 0) area[lat+1][lng] = TRUE;
		if (area[lat-1][lng] == 0) area[lat-1][lng] = TRUE;
		if (area[lat][lng+1] == 0) area[lat][lng+1] = TRUE;
		if (area[lat][lng-1] == 0) area[lat][lng-1] = TRUE;
		
		if (ref_lng % 2 == 0){
			if (area[lat-1][lng+1] == 0) area[lat-1][lng+1] = TRUE;
			if (area[lat-1][lng-1] == 0) area[lat-1][lng-1] = TRUE;			
		} else {
			if (area[lat+1][lng+1] == 0) area[lat+1][lng+1] = TRUE;
			if (area[lat+1][lng-1] == 0) area[lat+1][lng-1] = TRUE;			
		}
		return area;
	}

}
