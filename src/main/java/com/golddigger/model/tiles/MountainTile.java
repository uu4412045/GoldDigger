package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class MountainTile extends Tile {
	public static int DEFAULT_MOVEMENT_COST = 500;
	public static int DEFAULT_HEIGHT = 3;
	
	public MountainTile() {
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
	}
}
