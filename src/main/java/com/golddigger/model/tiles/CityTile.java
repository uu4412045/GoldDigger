package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class CityTile extends Tile{
	public static final int DEFAULT_MOVEMENT_COST = 200;
	public static final int DEFAULT_HEIGHT = 2;
	
	public CityTile() {
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
	}
}
