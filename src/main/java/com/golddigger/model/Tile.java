package com.golddigger.model;

public abstract class Tile {
	public static final int DEFAULT_MOVEMENT_COST = 100;
	public static final int DEFAULT_HEIGHT = 0;
	
	private int movementCost;
	private Coordinate teleportDestination = null;
	private int height = 0;
	
	/**
	 * New Tile with the default movement cost set
	 * @param movementCost The delay for a successful move in milliseconds
	 */
	public Tile(int movementCost, int height){
		this.movementCost = movementCost;
		this.height = height;
	}
	
	/**
	 * Is a unit allowed to move onto this tile
	 * @return true if yes, false if no
	 */
	public boolean isTreadable(){
		return true;
	}
	
	/**
	 * Used to store "tile types" in a hashmap in MoveService.java
	 */
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Return the default movement cost of this tile
	 * @return
	 */
	public int getDefaultMovementCost(){
		return this.movementCost;
	}

	/** The disadvantagous teleport location
	 * @return the destination coordinate, or null if this tile does not
	 * teleport.
	 */
	public Coordinate getTeleportDestination() {
		return teleportDestination;
	}
	
	/** Set the destination location of the disadvantageous teleport.
	 * @return the destination coordinate, or null if this tile should not
	 * teleport.
	 */
	public void setTeleportDestination(Coordinate teleportDestination) {
		this.teleportDestination = teleportDestination;
	}
	
	public int getHeight(){
		return this.height;
	}
}
