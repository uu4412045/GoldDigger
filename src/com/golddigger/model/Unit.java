package com.golddigger.model;

/**
 * Basic unit in the game. Has the ability to hold gold and is owned by a player.
 * @author Brett Wandel
 * @see Player
 */
public class Unit {
	private int x, y, gold=0;
	
	/**
	 * The player who owns this unit
	 */
	private Player owner;
	
	public Unit(Player owner, int x, int y){
		this.owner = owner;
		this.x = x;
		this.y = y;
	}
	
	public Unit(Player owner, Point2D pos){
		this(owner, pos.x, pos.y);
	}
	
	/**
	 * Set the position of this digger.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @return Get the current position of the unit
	 * @see Point2D
	 */
	public Point2D getPosition(){
		return new Point2D(x,y);
	}
	
	public int getX(){ return this.x;}
	public int getY(){ return this.y;}
	
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
}
