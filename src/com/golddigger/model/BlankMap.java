package com.golddigger.model;

import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.WallTile;

public class BlankMap extends Map {
	
	public BlankMap(int x, int y, int baseX, int baseY){
		this(x,y);
		setBase(baseX,baseY);
	}
	
	public BlankMap(int x, int y, int numberOfBases){
		this(x,y);
		
		int i=1,j=1;
		for (int n = 0; n < numberOfBases; n++){
			setBase(i,j);
			if (j == y-2){
				i++;
				j = 1;
			} else if (j < y-1){
				j++;
			}
		}
	}
	
	public BlankMap(int x, int y) {
		super(x, y);
		for (int i = 0; i <= getMaxX(); i++){
			for (int j = 0; j <= getMaxY(); j++) {
				if (i == 0 || i == getMaxX() || j == 0 || j == getMaxY()){
					tiles[i][j] = new WallTile();
				} else {
					tiles[i][j] = new GoldTile();
				}
			}
		}
		setBase(1,1);
	}
	
	public void setBase(int x, int y){
		tiles[x][y] = new BaseTile();
	}
}
