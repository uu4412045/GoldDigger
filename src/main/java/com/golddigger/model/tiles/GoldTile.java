package com.golddigger.model.tiles;

import com.golddigger.model.Tile;

public class GoldTile extends Tile {
	private int gold=0;

	public GoldTile(int qty){
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
		this.setGold(qty);
	}
	
	public GoldTile(){
		this(0);
	};
	
	public int getGold(){
		return this.gold;
	}
	
	public void setGold(int qty){
		if (qty < 0) {
			qty = 0;
		} else if (qty > 9) {
			qty = 9;
		}
		this.gold = qty;
	}
	
	@Override
	public String toString(){
		return super.toString()+"_"+gold;
	}
}
