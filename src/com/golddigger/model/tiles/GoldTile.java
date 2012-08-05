package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class GoldTile extends Tile {
	private int gold=0;
	
	public GoldTile(){};
	public GoldTile(int qty){this.setGold(qty);}
	public int getGold(){ return this.gold;}
	public void setGold(int qty){
		if (qty < 0) {
			qty = 0;
		} else if (qty > 9) {
			qty = 9;
		}
		this.gold = qty;
	}
}
