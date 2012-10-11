package com.golddigger.utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A Simple occlusion algorithm. uses the tile height of a square tiled
 * map to determine which tiles are occluded.
 * @author Brett Wandel
 */
public class SquareOcclusion {
	public static final double PRECISION = 0.001;
	private static final double DELTA = 1-(2*PRECISION);
	private int[][] heightMap;
	private boolean[][] mask;
	private int height, width;
	private Point2D centre;
	private int centreHeight = 0;
	private int centreLat, centreLng;
	
	private SquareOcclusion(int[][] heightMap){
		this.heightMap = heightMap;
		this.width = heightMap.length;
		this.height = heightMap[0].length;
		this.mask = new boolean[width][height];
		centreLat = (height-1)/2;
		centreLng = (width-1)/2;
		this.centre = new Point2D.Double(centreLat+.5, centreLng+.5);
		this.centreHeight = heightMap[(height-1)/2][(width-1)/2];
		
		for (int lat = 0; lat < this.width; lat++){
			for (int lng = 0; lng < this.height; lng++){
				this.mask[lat][lng] = checkOcclusion(lat,lng);
			}
		}
	}
	
	/**
	 * determine if any other tiles occlude this tile
	 * @param lat the latitude coordinate of the tile
	 * @param lng the longitude coordinate of the tile
	 * @return true if another tile does occlude this tile.
	 */
	private boolean checkOcclusion(int i, int j) {
		int targetTileHeight = heightMap[i][j];
		if (targetTileHeight == -1) return false;
		Point2D pos = new Point2D.Double(i+0.5,j+0.5);
		for (int lat = 0; lat < width; lat++){
			for (int lng = 0; lng < height; lng++){
				if (lat == centreLat && lng == centreLng) continue;
				if (lat == i && j == lng) continue;
				int blockingTileHeight = heightMap[lat][lng];
				if (blockingTileHeight == -1) continue;
				if (centreHeight > blockingTileHeight) continue;
				if ((blockingTileHeight == Integer.MAX_VALUE || blockingTileHeight > targetTileHeight) && isBehind(lat,lng, pos)) {
					return true;
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
	public static boolean[][] buildOcclusionMask(int[][] heightMap) {
		return new SquareOcclusion(heightMap).mask;
	}
	
	/**
	 * Check to see if the tile is between the target and center.
	 * @param lat the latitude of the tile to check
	 * @param lng the longitude of the tile to check
	 * @param target the target tile
	 * @return true if it is.
	 */
	private boolean isBehind(int lat, int lng, Point2D target){
		return new Line2D.Double(centre,target).intersects(lat+PRECISION,lng+PRECISION,DELTA,DELTA);
	}
}
