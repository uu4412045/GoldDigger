package com.golddigger.services;

import java.util.Map;

import com.golddigger.model.Direction;
/**
 * HexMoveService extends MoveService to provide the particular directions that
 * can be executed for hex tiles.
 * 
 * The core movement logic is contained in {@link MoveService}
 * 
 * @author Brett Wandel
 * @see MoveService
 */
public class HexMoveService extends MoveService{

	public HexMoveService(){
		super();
	}
	
	public HexMoveService(Map<String, Integer> costs){
		super(costs);
	}
	
	@Override
	public boolean isValidDirection(Direction direction) {
		if (direction == null) return false;
		return direction != Direction.EAST && direction != Direction.WEST;
	}

}
