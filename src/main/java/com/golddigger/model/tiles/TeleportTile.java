package com.golddigger.model.tiles;

import com.golddigger.model.Coordinate;

import com.golddigger.model.Tile;

public class TeleportTile extends Tile {
	private Coordinate destinationTile;
	private Tile mountedTile;
	
	public TeleportTile(Coordinate destination, Tile mountedTile) {
		super(DEFAULT_MOVEMENT_COST, DEFAULT_HEIGHT);
		this.destinationTile = destination;
		this.mountedTile = mountedTile;
	}
	
	/**
	 * @return the destination teleport tile this tile is linked to.
	 */
	public Coordinate getDestinationPos(){
		return this.destinationTile;
	}
	
	/**
	 * sets the position of the destination teleport tile
	 * @param pos
	 */
	public void setDestinationPos(Coordinate pos){
		this.destinationTile = pos;
	}
	
	/**
	 * sets the tile that this tile is on top of
	 * @param tile
	 */
	public void setMountedTile(Tile tile){
		this.mountedTile = tile;
	}
	
	/**
	 * @return the tile that this tile is on top of
	 */
	public Tile getMountedTile(){
		return this.mountedTile;
	}
}
