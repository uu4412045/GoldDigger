package com.golddigger.model.tiles;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;

public class BaseTile extends Tile {
	private Player owner = null;
	
	public BaseTile() {
		super(DEFAULT_MOVEMENT_COST);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
