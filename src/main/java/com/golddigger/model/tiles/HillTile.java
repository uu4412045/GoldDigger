package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class HillTile extends Tile {
	public static int DEFAULT_MOVEMENT_COST = 175;
	
	public HillTile() {
		super(DEFAULT_MOVEMENT_COST);
	}
}
