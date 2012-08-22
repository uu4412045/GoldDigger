package com.golddigger.model;

import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.WallTile;

/**
 * Simple Blank {@link Map} only used for automated tests.
 * 
 * @author Brett Wandel
 */
public class BlankMap extends Map {
	/**
	 * Create a new Map with a specific width, height and location for the base.
	 * @param x the width of the map.
	 * @param y the height of the map.
	 * @param baseX the x-coord of the base
	 * @param baseY the y-coord of the base
	 */
	public BlankMap(int x, int y, int baseX, int baseY){
		this(x,y);
		setBase(baseX,baseY);
	}
	
	/**
	 * Creates a new Map with specific width and height, with more than one base.
	 * @param x the width of the map
	 * @param y the height of the map
	 * @param numberOfBases the number of bases to be added.
	 */
	public BlankMap(int x, int y, int numberOfBases){
		this(x,y);
		
		int i=1,j=1;
		for (int n = 0; n < numberOfBases; n++){
			setBase(i,j);
			if (j == y-2){
				i++;
				j = 1;
			} else if (j < y-1){
				j++;
			}
		}
	}
	
	/**
	 * Create a base with a specific width and height, with a base at position (1,1)
	 * @param x width of the map
	 * @param y height of the map
	 */
	public BlankMap(int x, int y) {
		super(x, y);
		for (int i = 0; i <= getMaxX(); i++){
			for (int j = 0; j <= getMaxY(); j++) {
				if (i == 0 || i == getMaxX() || j == 0 || j == getMaxY()){
					tiles[i][j] = new WallTile();
				} else {
					tiles[i][j] = new GoldTile();
				}
			}
		}
		setBase(1,1);
	}
	
	/**
	 * put a base at (x,y).
	 * @param x x-coord of the new base
	 * @param y y-coord of the new base
	 */
	public void setBase(int x, int y){
		tiles[x][y] = new BaseTile();
	}
}
