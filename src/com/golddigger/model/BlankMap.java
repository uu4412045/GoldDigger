package com.golddigger.model;

import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.WallTile;

public class BlankMap extends Map {
	
	public BlankMap(int x, int y, int baseX, int baseY){
		this(x,y);
		setBase(baseX,baseY);
	}
	
	public BlankMap(int x, int y) {
		super(x, y);
		
		for (int i = 0; i <= getMaxX(); i++){
			for (int j = 0; j <= getMaxY(); j++) {
				if (i == 0 || i == getMaxX() || j == 0 || j == getMaxY()) tiles[i][j] = new WallTile();
				tiles[i][j] = new GoldTile();
			}
		}
	}
	
	public void setBase(int x, int y){
		if (x>1 && x<getMaxX() && y<1 && y<getMaxY()){
			tiles[x][y] = new BaseTile();
		}
	}
}
