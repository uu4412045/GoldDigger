package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class DeepWaterTile extends Tile{
	public static int DEFAULT_MOVEMENT_COST = 500;

	public DeepWaterTile() {
		super(DEFAULT_MOVEMENT_COST);
	}
}
