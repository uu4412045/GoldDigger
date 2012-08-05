package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class WallTile extends Tile {
	@Override
	public boolean isTreadable(){
		return false;
	}
}
