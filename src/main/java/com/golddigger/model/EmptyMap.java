package com.golddigger.model;

import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;

public class EmptyMap extends Map {
	public EmptyMap(int height, int width){
		super(height, width);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				set(i,j,new GoldTile());
			}
		}
	}
	
	public EmptyMap(int height, int width, int lat, int lng){
		this(height, width);
		set(lat,lng, new BaseTile());
	}
}
