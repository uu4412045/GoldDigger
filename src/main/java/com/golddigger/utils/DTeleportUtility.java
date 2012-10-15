package com.golddigger.utils;

import static com.golddigger.model.Direction.EAST;
import static com.golddigger.model.Direction.NORTH;
import static com.golddigger.model.Direction.NORTH_EAST;
import static com.golddigger.model.Direction.NORTH_WEST;
import static com.golddigger.model.Direction.SOUTH;
import static com.golddigger.model.Direction.SOUTH_EAST;
import static com.golddigger.model.Direction.SOUTH_WEST;
import static com.golddigger.model.Direction.WEST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.golddigger.model.Direction;
import com.golddigger.model.Map;
import com.golddigger.model.Coordinate;
import com.golddigger.model.Tile;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;

/**
 * This class contains some miscellaneous utility methods associated with the
 * teleportation. Originally for disadvantageous teleportation. Think about
 * creating a constructor with parameter map, since it is used often in the
 * methods.
 * 
 */

public class DTeleportUtility {

	/**
	 * Converts an array of strings of the form "xSrc,ySrc->xDst,yDst", where
	 * xSrc... are integers into source and destination coordinate pairs.
	 */
	public ArrayList<Coordinate[]> formatDTeleports(String[] teleports) {

		ArrayList<Coordinate[]> teleportPairs = new ArrayList<Coordinate[]>();
		String xSrc, xDst, ySrc, yDst;
		Coordinate src, dst;

		if (teleports == null) {
			return teleportPairs;
		}
		for (String pair : teleports) {
			pair = pair.replaceAll("\\s", "");
			String[] coords = pair.split("->");
			if (coords.length != 2) {
				System.out.println("Warning: teleportation pair: \"" + pair
						+ "\" is invalid");
				continue;
			}
			try {
				xSrc = coords[0].split(",")[0];
				ySrc = coords[0].split(",")[1];
				xDst = coords[1].split(",")[0];
				yDst = coords[1].split(",")[1];
			} catch (Exception e) {
				continue;
			}
			try {
				src = new Coordinate(Integer.parseInt(xSrc),
						Integer.parseInt(ySrc));
				dst = new Coordinate(Integer.parseInt(xDst),
						Integer.parseInt(yDst));
			} catch (NumberFormatException e) {
				System.out.println("Warning: teleportation pair: \"" + pair
						+ "\" is invalid");
				continue;
			}
			Coordinate[] coordPair = { src, dst };
			teleportPairs.add(coordPair);
		}
		return teleportPairs;
	}

	/**
	 * Links up the source tiles in the game map so they teleport to the
	 * destinations. Note that the teleportation pairs are assumed to be
	 * validated before this function and so it does not print warnings.
	 * 
	 * @param teleportPairs
	 *            the list of source and destination coordinate pairs, the
	 *            source teleports the digger to destination once the digger
	 *            steps on the source tile
	 * @param map
	 *            The game map which contains the tiles to be linked for
	 *            teleportation
	 */
	public void assignDTeleports(ArrayList<Coordinate[]> dTeleportPairs, Map map) {

		for (Coordinate[] pair : dTeleportPairs) {
			Coordinate src = pair[0];
			Coordinate dst = pair[1];
			boolean srcIsValid, dstIsValid;

			srcIsValid = tileIsValid(src, map)
					&& !(map.get(src) instanceof BaseTile);

			if (map.get(src) instanceof GoldTile
					&& ((GoldTile) map.get(src)).getGold() > 0) {
				srcIsValid = false;
			}

			dstIsValid = tileIsValid(dst, map);

			if (srcIsValid && dstIsValid) {
				map.get(src).setTeleportDestination(dst);
			}
		}

	}

	/**
	 * This method finds tiles that a digger can reach by normal movement
	 * (north, south, ...) starting from parameter location. The initial call to
	 * this method should have the digger's base as the location.
	 * 
	 * @param location
	 *            The starting location to see which tiles the digger can reach
	 *            from here by normal movement
	 * @param reachableTiles
	 *            The set that holds tiles that are reachable from location (so
	 *            this method can be called recursively) - note that all "tiles"
	 *            in this set have already been visited
	 * @param map
	 *            The game map that is to be traversed
	 * @param teleportPairs
	 *            The list of all disadvantageous teleport pairs in this map, if
	 *            there are no teleports give an empty list
	 * @param numberOfSides
	 *            Hex or square map
	 * 
	 */
	public void findReachableTiles(Coordinate location,
			HashSet<String> reachableTiles, Map map,
			ArrayList<Coordinate[]> teleportPairs, int numberOfSides) {

		String squareIndex = location.toString();

		if (tileIsValid(location, map) && !reachableTiles.contains(squareIndex)) {

			reachableTiles.add(squareIndex);

			if (!(map.get(location) instanceof BaseTile)
					&& getDestination(location, teleportPairs) != null) {

				/*
				 * This tile will teleport somewhere so adjacent tiles cannot be
				 * reached from here by normal movement.
				 */
				return;
			}

			/*
			 * Move onto adjacent tiles until all reachable tiles are visited
			 */
			if (numberOfSides == 4) {
				findReachableTiles(NORTH.getOffset(location), reachableTiles,
						map, teleportPairs, numberOfSides);
				findReachableTiles(EAST.getOffset(location), reachableTiles,
						map, teleportPairs, numberOfSides);
				findReachableTiles(WEST.getOffset(location), reachableTiles,
						map, teleportPairs, numberOfSides);
				findReachableTiles(SOUTH.getOffset(location), reachableTiles,
						map, teleportPairs, numberOfSides);
			}
			if (numberOfSides == 6) {
				findReachableTiles(NORTH.getOffset(location), reachableTiles,
						map, teleportPairs, numberOfSides);
				findReachableTiles(NORTH_EAST.getOffset(location),
						reachableTiles, map, teleportPairs, numberOfSides);
				findReachableTiles(NORTH_WEST.getOffset(location),
						reachableTiles, map, teleportPairs, numberOfSides);
				findReachableTiles(SOUTH.getOffset(location), reachableTiles,
						map, teleportPairs, numberOfSides);
				findReachableTiles(SOUTH_EAST.getOffset(location),
						reachableTiles, map, teleportPairs, numberOfSides);
				findReachableTiles(SOUTH_WEST.getOffset(location),
						reachableTiles, map, teleportPairs, numberOfSides);
			}
		}
	}

	/**
	 * Get the bases (tiles) from the map, similar function in class Game
	 * 
	 * @param map
	 *            The map to get the bases from
	 * @return An ArrayList of the base tiles on the map
	 */
	public ArrayList<BaseTile> getBases(Map map) {

		ArrayList<BaseTile> bases = new ArrayList<BaseTile>();

		for (Tile[] row : map.getTiles()) {
			for (Tile tile : row) {
				if (tile instanceof BaseTile) {
					bases.add((BaseTile) tile);
				}
			}
		}

		return bases;
	}

	/**
	 * Get the non empty gold tiles from the map
	 * 
	 * @param map
	 *            The map to get the gold tiles from
	 * @return An ArrayList of the gold tiles
	 */
	public ArrayList<GoldTile> getGoldTiles(Map map) {

		ArrayList<GoldTile> goldTiles = new ArrayList<GoldTile>();

		for (Tile[] row : map.getTiles()) {
			for (Tile tile : row) {
				if (tile instanceof GoldTile && ((GoldTile) tile).getGold() > 0) {
					goldTiles.add((GoldTile) tile);
				}
			}
		}

		return goldTiles;
	}

	/**
	 * Get the destination coordinate if there exists a pair in teleportPairs
	 * list with the parameter source coordinate. Return the last if more than
	 * one pair exists.
	 * 
	 * @param source
	 *            The source Point2D coordinate to look for
	 * @param teleportPairs
	 *            The ArrayList to look in
	 * @return The destination coordinate if there exists a pair in
	 *         teleportPairs with a matching source or null otherwise
	 */
	public Coordinate getDestination(Coordinate source,
			ArrayList<Coordinate[]> teleportPairs) {

		Coordinate destination = null;

		for (Coordinate[] pair : teleportPairs) {
			if (pair.length == 2 && pair[0].equals(source)) {
				destination = pair[1];
			}
		}

		return destination;

	}

	/**
	 * Checks that a tile in a map is valid
	 * 
	 * @param tile
	 *            The coordinates of the tile to validate
	 * @param map
	 *            The game map that the tile should be validated against
	 * @return true if parameter tile is not null, is in the map and is
	 *         treadable, false otherwise
	 */
	public boolean tileIsValid(Coordinate tile, Map map) {

		boolean tileIsValid;

		if (tile != null && map.get(tile) != null
				&& map.get(tile).isTreadable()) {
			tileIsValid = true;
		} else {
			tileIsValid = false;
		}

		return tileIsValid;

	}

	/**
	 * Filters a list of disadvantageous teleport pairs based on whether they
	 * are invalid in the sense that they are not walls, in the map, do not
	 * teleport to trapped areas and do not trap gold
	 * 
	 * @param dTeleportPairs
	 *            The teleport source and destination coordinate pairs
	 * @param map
	 *            The game map to check the teleport mappings against
	 * @param numberOfSides
	 *            Hex or square map
	 * @return The list of valid teleports, with invalid ones removed
	 */
	public ArrayList<Coordinate[]> validDTeleports(
			ArrayList<Coordinate[]> dTeleportPairs, Map map, int numberOfSides) {

		System.out.println("Validating disadvantageous teleports...");

		ArrayList<Coordinate[]> validTeleports = new ArrayList<Coordinate[]>();
		ArrayList<BaseTile> bases = getBases(map);
		ArrayList<GoldTile> goldTiles = getGoldTiles(map);

		for (BaseTile base : bases) {

			System.out.println("Validating disadvantageous teleports for base: "
							+ map.getPosition(base).toString());

			for (Coordinate[] pair : dTeleportPairs) {

				validTeleports.add(pair);

				HashSet<String> reachableTiles = new HashSet<String>();
				Coordinate baseCoordinate = map.getPosition(base);

				/* Check that the pair itself is valid */
				if (pair.length != 2) {
					validTeleports.remove(pair);
					continue;
				}

				Coordinate src = pair[0];
				Coordinate dst = pair[1];
				boolean srcIsValid = tileIsValid(src, map);
				boolean dstIsValid = tileIsValid(dst, map);

				if ((srcIsValid && src.equals(baseCoordinate)) || !srcIsValid
						|| !dstIsValid) {
					printWarning(pair, "Source is a base tile,"
							+ " or source and destination are not treadable"
							+ " or are not in the map");
					validTeleports.remove(pair);
					continue;
				}

				if (map.get(src) instanceof GoldTile
						&& ((GoldTile) map.get(src)).getGold() > 0) {
					printWarning(pair, "Source is a gold tile");
					validTeleports.remove(pair);
					continue;
				}

				/* Now check for traps */

				/*
				 * If the source is not reachable from this base then do not
				 * worry about this teleport pair, since a digger will not be
				 * able to reach the source tile the digger cannot be
				 * teleported. However, if the source can be reached but the
				 * digger cannot get back to base from the destination by normal
				 * movement then the digger is trapped so remove the pair.
				 */
				findReachableTiles(baseCoordinate, reachableTiles, map,
						validTeleports, numberOfSides);

				if (reachableTiles.contains(src.toString())
						&& !(reachableTiles.contains(dst.toString()))) {
					printWarning(pair, "Traps digger");
					validTeleports.remove(pair);
					continue;
				}

				/*
				 * Check that the teleport sorce does not block non empty gold
				 * tiles
				 */
				for (GoldTile goldTile : goldTiles) {
					if (reachableTiles.contains(src.toString())
							&& !reachableTiles.contains(map.getPosition(
									goldTile).toString())) {
						printWarning(pair, "Traps gold");
						validTeleports.remove(pair);
						continue;
					}
				}
			}
		}

		System.out.println("Validating disadvantageous teleports done");
		return validTeleports;
	}

	private void printWarning(Coordinate[] pair, String message) {
		System.out.println("Warning: disadvantageous teleport pair "
				+ pair[0].toString() + "->" + pair[1].toString()
				+ " is invalid, being dropped." + " " + message);
	}
}
