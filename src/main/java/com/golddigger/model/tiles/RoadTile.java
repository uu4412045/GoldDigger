package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class RoadTile extends Tile {
	public static int DEFAULT_MOVEMENT_COST = 25;

	public RoadTile() {
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
	}
}
