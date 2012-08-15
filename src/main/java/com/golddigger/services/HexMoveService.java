package com.golddigger.services;

import java.util.Map;

import com.golddigger.model.Direction;

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
