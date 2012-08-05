package com.golddigger.model;

import com.golddigger.model.tiles.GoldTile;

public class Map {
	protected Tile[][] tiles;
	
	public Map(int x, int y){
		tiles = new Tile[x][y];
	}
	
	public Tile get(int x, int y){
		if(!inBounds(x,y)) return null;
		else return tiles[x][y];
	}
	
	public void set(int x, int y, Tile tile){
		if(inBounds(x,y)) tiles[x][y] = tile;
	}
	
	private boolean inBounds(int x, int y){
		return x > -1 && y > -1 && x < tiles.length && y < tiles[0].length;
	}
	
	protected Tile[][] getTiles(){
		return this.tiles;
	}
	
	public int getMaxX(){
		return tiles.length-1;
	}
	
	public int getMaxY(){
		return this.tiles[0].length-1;
	}
	
	public Tile[][] getArea(int x, int y, int r){
		if (r < 1) return null;

		int size = (2*r)+1;
		Tile[][] area = new Tile[size][size];
		for (int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				area[i][j] = get(x+i-r, y+j-r);
			}
		}
		return area;
	}
	
	public boolean hasGoldLeft(){
		for (Tile[] row:tiles){
			for (Tile tile:row){
				if (tile instanceof GoldTile){
					if (((GoldTile) tile).getGold() > 0) return true; 
				}
			}
		}
		return false;
	}
}
