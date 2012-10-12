package com.golddigger.model;

import com.golddigger.model.tiles.TeleportTile;

/**
 * Basic unit in the game. Has the ability to hold gold and is owned by a player.
 * @author Brett Wandel
 * @see Player
 */
public class Unit {
	private int lat, lng, gold=0;
	private TeleportTile teleportTile;
	
	/**
	 * The player who owns this unit
	 */
	private Player owner;

	
	public Unit(Player owner, int lat, int lng){
		this.owner = owner;
		this.lat = lat;
		this.lng = lng;
	}
	
	public Unit(Player owner, Coordinate pos){
		this(owner, pos.lat, pos.lng);
	}
	
	/**
	 * Set the position of this digger.
	 * @param lat The latitude
	 * @param lng The longitude
	 */
	public void setPosition(int lat, int lng){
		this.lat = lat;
		this.lng = lng;
	}
	
	/**
	 * 
	 * @return Get the current position of the unit
	 * @see Coordinate
	 */
	public Coordinate getPosition(){
		return new Coordinate(lat,lng);
	}
	
	public int getLat(){ return this.lat;}
	public int getLng(){ return this.lng;}
	
	/**
	 * Check to see if the unit is owned by the specified {@link Player}
	 */
	public boolean isOwnedBy(Player player) {
		return this.owner == player;
	}
	
	/**
	 * Set how much gold this unit is carrying.
	 * @param qty the quantity of gold.
	 */
	public void setGold(int qty){
		this.gold = qty;
	}
	
	/**
	 * @return the quantity of gold this unit is carrying.
	 */
	public int getGold(){
		return this.gold;
	}

	/**
	 * set the position of the unit
	 * @param location The target location.
	 */
	public void setPosition(Coordinate location) {
		setPosition(location.lat, location.lng);
	}
	
	/**
	 * Get the player who owns this unit
	 * @param The owner
     */
	public Player getOwner() {
		return this.owner;
	}
	
	/**
	 * @return whether or not the unit is carrying a teleport tile
	 */
	public boolean isCarryingTeleportTile(){
		return teleportTile != null ? true : false;
	}
	
	/**
	 * @return the teleport Tile that the digger is carrying
	 */
	public TeleportTile getTeleportTile(){
		return teleportTile;
	}
	
	/**
	 * Set the unit's teleport tile
	 * @param tile
	 */
	public void setTeleportTile(TeleportTile tile){
		this.teleportTile = tile;
	}
}
