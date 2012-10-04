package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class WallTile extends Tile {
	
	public WallTile() {
		super(DEFAULT_MOVEMENT_COST);
	}

	@Override
	public boolean isTreadable(){
		return false;
	}
}
