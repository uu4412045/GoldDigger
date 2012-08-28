package com.golddigger.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.WallTile;

public class BlankMapTest {

	@Test
	public void testNormal() {
		Map map = new BlankMap(10,20);
		assertEquals(9, map.getHeight());
		assertEquals(19, map.getWidth());

		assertTrue(map.get(0,0) instanceof WallTile);
		assertTrue(map.get(0,19) instanceof WallTile);
		assertTrue(map.get(9,0) instanceof WallTile);
		assertTrue(map.get(9,9) instanceof WallTile);
		assertTrue(map.get(1,1) instanceof BaseTile);
		assertTrue(map.get(1,18) instanceof GoldTile);
		assertTrue(map.get(8,1) instanceof GoldTile);
		assertTrue(map.get(8,18) instanceof GoldTile);
		
	}
	
	@Test
	public void testBases(){
		Map map = new BlankMap(6,4,6);
		assertTrue(map.get(1,1) instanceof BaseTile);
		assertTrue(map.get(1,2) instanceof BaseTile);
		assertTrue(map.get(2,1) instanceof BaseTile);
		assertTrue(map.get(3,1) instanceof BaseTile);
		assertTrue(map.get(3,2) instanceof BaseTile);
		assertTrue(map.get(4,1) instanceof GoldTile);
		assertTrue(map.get(4,2) instanceof GoldTile);
	}

}
