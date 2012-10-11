package com.golddigger.utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import com.golddigger.model.Coordinate;

/**
 * A Simple occlusion algorithm. uses the tile height of a hexagon tiled
 * map to determine which tiles are occluded.
 * @author Brett Wandel
 */
public class HexOcclusion {
	private int[][] heightMap;
	private boolean[][] mask;
	private int width, height;
	private int lat_offset, lng_offset;
	private int centreHeight = 0;
	private Point2D centre;
	private int centreLat, centreLng;

	private HexOcclusion(int[][] heightMap, Coordinate location){
		this.heightMap = heightMap;
		this.height = heightMap.length;
		this.width = heightMap[0].length;
		this.mask = new boolean[height][width];
		centreLat= (height-1)/2;
		centreLng = (width-1)/2;
		this.centreHeight = heightMap[centreLat][centreLng];

		this.centre = new Hexagon(location).getCenter();
		this.lat_offset = location.lat - ((height-1)/2);
		this.lng_offset = location.lng - ((width-1)/2);
		
		for (int lat = 0; lat < this.height; lat++){
			for (int lng = 0; lng < this.width; lng++){
				this.mask[lat][lng] = checkOcclusion(lat,lng);
			}
		}
	}
	
	/**
	 * determine if any other tiles occlude this tile
	 * @param i the latitude coordinate of the tile
	 * @param j the longitude coordinate of the tile
	 * @return true if another tile does occlude this tile.
	 */
	private boolean checkOcclusion(int i, int j) {
		int currentTileHeight = heightMap[i][j];
		if (currentTileHeight == -1) return false;
		for (int lat = 0; lat < height; lat++){
			for (int lng = 0; lng < width; lng++){
				if (lat == i && j == lng) continue;
				if (lat == centreLat && lng == centreLng) continue;
				int blockingTileHeight = heightMap[lat][lng];
				if (blockingTileHeight == -1) continue;
				if (centreHeight > blockingTileHeight) continue;
				if (blockingTileHeight == Integer.MAX_VALUE || blockingTileHeight > currentTileHeight){
					Point2D pos = new Hexagon(lat_offset+i,lng_offset+j).getCenter();
					if (isBehind(lat, lng, pos)){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Build an occlusion mask from the given heights. 
	 *
	 * <p>If you don't want a tile checked/occluded, pass in -1 as the height. This is usually used
	 * for the tiles that are out of the maps bounds.</p>
	 * 
	 * @param heightMap a 2D array containing the heights for each tile.
	 * @return a true/false value for each tile in the array. true if the tile is occluded.
	 */
	public static boolean[][] create(int[][] heightMap, Coordinate center) {
		return new HexOcclusion(heightMap, center).mask;
	}

	/**
	 * Check to see if the tile is between the target and center.
	 * @param lat the latitude of the tile to check
	 * @param lng the longitude of the tile to check
	 * @param target the target tile
	 * @return true if it is.
	 */
	private boolean isBehind(int lat, int lng, Point2D target){
		Line2D line = new Line2D.Double(centre, target);
		Hexagon blocker = new Hexagon(lat_offset+lat,lng_offset+lng);
		return blocker.intersects(line);
	}
}
