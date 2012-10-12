package com.golddigger.utils;

import static com.golddigger.model.Direction.*;

import java.util.ArrayList;
import java.util.List;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;
import com.golddigger.model.Map;
import com.golddigger.model.tiles.BaseTile;

/**
 * Simple utility that generates a list of all reachable coordinates
 *  on a map from the first base.
 * @author Brett Wandel
 *
 */
public class ATeleportUtility {
	private Direction[] neighbours;
	private Map map;
	private List<Coordinate> reachable;
	
	public ATeleportUtility(Map map, boolean hexagonTiles){
		this.map = map;
		if (hexagonTiles){
			neighbours = new Direction[]{NORTH, SOUTH, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST};
		} else {
			neighbours = new Direction[]{NORTH, SOUTH, EAST, WEST};
		}
		
		Coordinate base = getFirstBase();
		reachable = new ArrayList<Coordinate>();
		if (base != null){
			findReachableTiles(base);
		}
	}
	
	/**
	 * Is the coordinate reachable by normal movement.
	 * @param coordinate
	 * @return
	 */
	public boolean isReachable(Coordinate coordinate){
		return reachable.contains(coordinate);
	}
	
	private void findReachableTiles(Coordinate coord){
		if (map.get(coord).isTreadable()) reachable.add(coord);
		else return;

		Coordinate next;
		for (Direction direction : neighbours){
			next = direction.getOffset(coord);
			if (reachable.contains(next)) continue;
			findReachableTiles(next);
		}
	}
	
	private Coordinate getFirstBase(){
		for (int lat = 0; lat < map.getHeight(); lat++){
			for (int lng = 0; lng < map.getWidth(); lng ++){
				if (map.get(lat,lng) instanceof BaseTile){
					return new Coordinate(lat, lng);
				}
			}
		}
		return null;
	}
}
