package com.golddigger.model;

import com.golddigger.model.tiles.GoldTile;
/**
 * Map is the simple representation of a "field" or "world" using tiles.<br />
 * </br>
 * The map is always square, and should have a boundary of {@link WallTile}.
 * 
 * @author Brett Wandel
 *
 */
public class Map {
	/**
	 * The tiles that make up the field. 
	 */
	protected Tile[][] tiles;
	
	/**
	 * create a new map
	 * @param x Width of the map
	 * @param y Height of the map
	 */
	public Map(int x, int y){
		tiles = new Tile[x][y];
	}
	
	/**
	 * Get a particular {@link Tile} at the given coordinates.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return <b>null</b> if is outside of the map boundaries.
	 * @see Tile
	 */
	public Tile get(int x, int y){
		if(!inBounds(x,y)) return null;
		else return tiles[x][y];
	}
	
	/**
	 * Sets a particular {@link Tile} at the given coordinates.<br />
	 * No effect if its outside the maps boundaries.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param tile The tile to set at this location.
	 * @see Tile
	 */
	public void set(int x, int y, Tile tile){
		if(inBounds(x,y)) tiles[x][y] = tile;
	}
	
	/**
	 * Checks to see if a location is inside the maps boundaries
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return <b>true</b> if it is, <b>false if its not</b>.
	 */
	protected boolean inBounds(int x, int y){
		return x > -1 && y > -1 && x < tiles.length && y < tiles[0].length;
	}
	
	/**
	 * @return all the tiles on the entire map.
	 * @see Tile
	 */
	public Tile[][] getTiles(){
		return this.tiles;
	}
	
	/**
	 * @return The width of the map.
	 */
	public int getMaxX(){
		return tiles.length-1;
	}
	
	/**
	 * @return The height of the map
	 */
	public int getMaxY(){
		return this.tiles[0].length-1;
	}
	
	/**
	 * Returns a particular area of the map.
	 * 
	 * based on radius, e.g:<br \>
	 * <ul>
	 *  <li>r=1 would return a 3x3 grid</li>
	 *  <li>r=2 would return a 5x5 grid</li>
	 *  <li>r=3 would return a 7x7 grid</li>
	 * </ul>
	 * @param x The x coordinate of the center tile
	 * @param y The y coordinate of the center tile
	 * @param r The radius of the area to get.
	 * @return The area specified. <b>null</b> if r < 1.
	 */
	public Tile[][] getArea(int x, int y, int r){
		if (r < 1) return null;

		int size = (2*r)+1;
		Tile[][] area = new Tile[size][size];
		for (int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				area[i][j] = get(x+i-r, y+j-r);
			}
		}
		return area;
	}
	
	/**
	 * Check to see if there is any {@link GoldTile}s that still have gold left.
	 * @return <b>true</b> if yes, <b>false</b> if no.
	 */
	public boolean hasGoldLeft(){
		for (Tile[] row:tiles){
			for (Tile tile:row){
				if (tile instanceof GoldTile){
					if (((GoldTile) tile).getGold() > 0) return true; 
				}
			}
		}
		return false;
	}

	public Point2D getPostion(Tile tile) {
		for (int x = 0; x <= getMaxX(); x++){
			for (int y = 0; y <= getMaxY(); y++){
				if (get(x, y) == tile){
					return new Point2D(x, y);
				}
			}
		}
		return null;
	}
}
