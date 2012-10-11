package com.golddigger.model.tiles;
import com.golddigger.model.Tile;

public class ShallowWaterTile extends Tile {
	public static int DEFAULT_MOVEMENT_COST = 150;
	
	public ShallowWaterTile() {
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
	}
}
