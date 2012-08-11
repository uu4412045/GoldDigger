package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.server.GameService;

public class HexViewService extends GameService {
	public static final String ACTION_TEXT = "view";
	public static final int DEFAULT_LINE_OF_SIGHT = 1;
	private static int CHECK=1, CHECKED=2;
	private int lineOfSight;

	public HexViewService() {
		this(DEFAULT_LINE_OF_SIGHT);
	}
	
	public HexViewService(int lineOfSight){
		super(BASE_PRIORITY);
		this.lineOfSight = lineOfSight;
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

		Tile[][] area = game.getMap().getArea(unit.getX(), unit.getY(), lineOfSight);
		int[][] hex = mask((area.length-1)/2);
		return true;
	}
	
	/**
	 * need to rewrite for variable LOS
	 * @param area
	 * @return
	 */
	public static int[][] mask(int radius){
		int size = (radius*2)+1;
		int[][] mask = new int[size][size];
		mask[radius][radius] = CHECK;
		
		for (int i = 0; i < radius-3; i++){
			for (int x = 0; x < size; x++){
				for (int y = 0; y < size; y++) {
					if (mask[x][y] == CHECK) mask = markNeighbours(mask,x,y);
				}
			}
		}
		return mask;
	}
	
	
	
	private static int[][] markNeighbours(int[][] area, int x, int y){
		area[x][y] = CHECKED;
		area[x+1][y] = CHECK;
		area[x][y+1] = CHECK;
		if (x % 2 == 0){
			area[x+1][y-1] = CHECK;
			area[x+1][y] = CHECK;
			area[x-1][y-1] = CHECK;
			area[x-1][y] = CHECK;
		} else {
			area[x+1][y] = CHECK;
			area[x+1][y+1] = CHECK;
			area[x-1][y] = CHECK;
			area[x-1][y+1] = CHECK;
		}
		return area;
	}

}
