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
	 * @param height Height of the map
	 * @param width Width of the map
	 */
	public Map(int height, int width){
		tiles = new Tile[height][width];
	}
	
	/**
	 * Get a particular {@link Tile} at the given coordinates.
	 * @param lat The latitude
	 * @param lng The longitude
	 * @return <b>null</b> if is outside of the map boundaries.
	 * @see Tile
	 */
	public Tile get(int lat, int lng){
		if(!inBounds(lat,lng)) return null;
		else return tiles[lat][lng];
	}
	
	/**
	 * Sets a particular {@link Tile} at the given coordinates.<br />
	 * No effect if its outside the maps boundaries.
	 * @param lat The latitude
	 * @param lng The longitude
	 * @param tile The tile to set at this location.
	 * @see Tile
	 */
	public void set(int lat, int lng, Tile tile){
		if(inBounds(lat,lng)) tiles[lat][lng] = tile;
	}
	
	/**
	 * Checks to see if a location is inside the maps boundaries
	 * @param lat The latitude
	 * @param lng The longitude
	 * @return <b>true</b> if it is, <b>false if its not</b>.
	 */
	protected boolean inBounds(int lat, int lng){
		return lat > -1 && lng > -1 && lat < tiles.length && lng < tiles[lat].length;
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
	public int getWidth(){
		return this.tiles[getHeight()].length-1;
	}
	
	/**
	 * @return The height of the map
	 */
	public int getHeight(){
		return tiles.length-1;
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
	 * @param lat The latitude of the center tile
	 * @param lng The longitude of the center tile
	 * @param r The radius of the area to get.
	 * @return The area specified. <b>null</b> if r < 1.
	 */
	public Tile[][] getArea(int lat, int lng, int r){
		if (r < 1) return null;

		int size = (2*r)+1;
		Tile[][] area = new Tile[size][size];
		for (int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				area[i][j] = get(lat+i-r, lng+j-r);
			}
		}
		return area;
	}
	
	public Tile[][] getArea(Point2D pos, int r){
		return getArea(pos.lat, pos.lng, r);
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

	/**
	 * returns the coordinate of the given tile
	 * @param tile the tile you want the position of
	 * @return <b>null</b> if the tile does not exist in this map.
	 */
	public Point2D getPosition(Tile tile) {
		for (int lat = 0; lat <= getHeight(); lat++){
			for (int lng = 0; lng <= getWidth(); lng++){
				if (get(lat, lng) == tile){
					return new Point2D(lat, lng);
				}
			}
		}
		return null;
	}

	/**
	 * returns the tile at the given location
	 * @param location A Point2D containing the coordinate of the tile to get.
	 * @return the tile at the location, or null if its outside the map bounds.
	 */
	public Tile get(Point2D location) {
		return get(location.lat, location.lng);
	}
}
