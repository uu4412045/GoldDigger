package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class OccludedTile extends Tile {
	public static final int DEFAULT_HEIGHT = -1;

	public OccludedTile() {
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
	}
}
