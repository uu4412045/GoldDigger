package com.golddigger.model;

import com.golddigger.model.tiles.GoldTile;

public class BlankMap extends Map {

	public BlankMap(int x, int y) {
		super(x, y);
		
		for (int i = 0; i <= getMaxX(); i++){
			for (int j = 0; j <= getMaxY(); j++) {
				tiles[i][j] = blank();
			}
		}
	}
	
	private Tile blank(){
		return new GoldTile();
	}

}
