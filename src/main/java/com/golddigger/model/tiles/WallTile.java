package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class WallTile extends Tile {
	public static final int DEFAULT_HEIGHT = Integer.MAX_VALUE;
	
	public WallTile() {
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
	}

	@Override
	public boolean isTreadable(){
		return false;
	}
}
