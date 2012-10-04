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
	 * @param width the width of the map.
	 * @param height the height of the map.
	 * @param baseLat the latitude of the base
	 * @param baseLng the longitude of the base
	 */
	public BlankMap(int width, int height, int baseLat, int baseLng){
		this(width,height, false);
		setBase(baseLat,baseLng);
	}

	/**
	 * Creates a new Map with specific width and height, with more than one base.
	 * @param width the width of the map
	 * @param height the height of the map
	 * @param numberOfBases the number of bases to be added.
	 */
	public BlankMap(int width, int height, int numberOfBases){
		this(width,height, false);

		int lat=1,lng=1;
		for (int n = 0; n < numberOfBases; n++){
			setBase(lat,lng);
			if (lng == height-2){
				lat++;
				lng = 1;
			} else if (lng < height-1){
				lng++;
			}
		}
	}

	public BlankMap(int height, int width){
		this(height, width, true);
	}

	/**
	 * Create a base with a specific width and height, with a base at position (1,1)
	 * @param height height of the map
	 * @param width width of the map
	 */
	private BlankMap(int height, int width, boolean base) {
		super(height, width);
		for (int lat = 0; lat < height; lat++){
			for (int lng = 0; lng < width; lng++) {
				if (lat == 0 || lat == getHeight() || lng == 0 || lng == getWidth()){
					tiles[lat][lng] = new WallTile();
				} else {
					tiles[lat][lng] = new GoldTile();
				}
			}
		}
		if (base) setBase(1,1);
	}

	/**
	 * put a base at (x,y).
	 * @param lat the latitude of the new base
	 * @param lng the longitude of the new base
	 */
	public void setBase(int lat, int lng){
		tiles[lat][lng] = new BaseTile();
	}
}
