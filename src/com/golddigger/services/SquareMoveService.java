package com.golddigger.services;

import java.util.Map;

import com.golddigger.model.Direction;

/**
 * This service will move the {@link Player}'s {@link Unit} in a particular direction. <br />
 * Each tile has a movement cost which will delay the servers response to simulate different terrain.<br />
 * Will return: <br />
 * <ul>
 * 	<li>"FAILED" if the direction is invalid</li>
 *  <li>"FAILED" if the {@link Tile} is not "treadable"</li>
 *  <li>"FAILED" if the position is out of the maps boundaries.</li>
 * </ul>
 * @author Brett Wandel
 */
public class SquareMoveService extends MoveService {
	
	public SquareMoveService(){
		super();
	}
	
	public SquareMoveService(Map<String, Integer> costs){
		super(costs);
	}
	
	@Override
	public boolean isValidDirection(Direction direction) {
		if (direction == null) return false;
		switch (direction){
		case NORTH: return true;
		case SOUTH: return true;
		case WEST: return true;
		case EAST: return true;
		default: return false;
		}
	}
	
}
