package com.golddigger.model;
/**
 * Direction is used for a simple way to work with directional movement in the game.
 * it has directions for both square tiles and hex tiles.
 * @author Brett Wandel
 */
public enum Direction{
	NORTH,SOUTH,EAST,WEST,NORTH_EAST,SOUTH_EAST,NORTH_WEST,SOUTH_WEST;
	
	/**
	 * Parse a direction from a string, usually from the URL.
	 * simply looks for a case-<b>insenstive</b> string of the same name. 
	 * @param string The string to be parsed
	 * @return <b>null</b> if the string cant be parsed, otherwise the appropriate direction.
	 */
	public static Direction parse(String string){
		if (string.equalsIgnoreCase(NORTH.toString())) return NORTH;
		if (string.equalsIgnoreCase(SOUTH.toString())) return SOUTH;
		if (string.equalsIgnoreCase(EAST.toString())) return EAST;
		if (string.equalsIgnoreCase(WEST.toString())) return WEST;
		if (string.equalsIgnoreCase(NORTH_EAST.toString())) return NORTH_EAST;
		if (string.equalsIgnoreCase(SOUTH_EAST.toString())) return SOUTH_EAST;
		if (string.equalsIgnoreCase(NORTH_WEST.toString())) return NORTH_WEST;
		if (string.equalsIgnoreCase(SOUTH_WEST.toString())) return SOUTH_WEST;
		return null;
	}

	/**
	 * checks to see if the direction is valid for hex tiles.
	 * @return true if it is.
	 */
	public boolean isHex() {
		return this != EAST && this != WEST;
	}
	
	/**
	 * offsets the position in the direction.
	 * @param position The position to offset
	 * @return
	 */
	// TODO: Write a better description
	public Coordinate getOffset(Coordinate position){
		int i = position.lng % 2;
		
		switch(this){
		case NORTH: return position.add(-1,0);
		case SOUTH: return position.add(1,0);
		case EAST: return position.add(0,1);
		case WEST: return position.add(0,-1);
		case NORTH_EAST: return position.add(i-1, 1);
		case SOUTH_EAST :return position.add(i, 1);
		case NORTH_WEST: return position.add(i-1, -1);
		case SOUTH_WEST: return position.add(i,-1);
		default: return position;
		}
	}
}