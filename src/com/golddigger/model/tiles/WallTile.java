package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class WallTile extends Tile {
	
	
	public WallTile() {
		super(0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isTreadable(){
		return false;
	}
}
