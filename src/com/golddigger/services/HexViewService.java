package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.GameService;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;

public class HexViewService extends GameService {
	public static final String ACTION_TEXT = "view";
	public static final int DEFAULT_LINE_OF_SIGHT = 1;
	private static final int UP=0,UP_RIGHT=1, DOWN_RIGHT=2, DOWN=3, DOWN_LEFT=4, UP_LEFT = 5;
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
		Tile[][] hex = trim(area);
		
		return true;
	}
	
	/**
	 * need to rewrite for variable LOS
	 * @param area
	 * @return
	 */
	private Tile[][] trim(Tile[][] area){
		Tile[][] hex = new Tile[][]{};
		
		return hex;
	}
	
	public Tile[] getNeighbours(Tile[][] area, int x, int y){
		Tile[] neighbours = new Tile[6];
		neighbours[UP] = area[x+1][y];
		neighbours[DOWN] = area[x][y+1];
		if (x % 2 == 0){
			neighbours[UP_RIGHT]   = area[x+1][y-1];
			neighbours[DOWN_RIGHT] = area[x+1][y];
			neighbours[UP_LEFT]    = area[x-1][y-1];
			neighbours[DOWN_LEFT]  = area[x-1][y];
		} else {
			neighbours[UP_RIGHT]   = area[x+1][y];
			neighbours[DOWN_RIGHT] = area[x+1][y+1];
			neighbours[UP_LEFT]    = area[x-1][y];
			neighbours[DOWN_LEFT]  = area[x-1][y+1];
			
		}
		return neighbours;
	}

}
