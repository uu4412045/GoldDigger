package com.golddigger.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapTest {
	
	@Test
	public void testBounds() {
		Map map = new BlankMap(10,10);
		assertNull(map.get(-1, 0));
		assertNull(map.get(0, -1));
		assertNull(map.get(map.getMaxX()+1, 0));
		assertNull(map.get(0, map.getMaxY()+1));
		assertNotNull(map.get(0, 0));
		assertNotNull(map.get(map.getMaxX(), 0));
		assertNotNull(map.get(0, map.getMaxY()));
		assertNotNull(map.get(map.getMaxX(), map.getMaxY()));
	}
	
	@Test
	public void testArea(){
		Map map = new BlankMap(10,10);
		
		Tile[][] area = map.getArea(9, 9, 1);
		assertNotNull(area[0][0]);
		assertEquals(map.get(8, 8), area[0][0]);
		assertNotNull(area[0][1]);
		assertEquals(map.get(8, 9), area[0][1]);
		assertNull(area[0][2]);
		assertNotNull(area[1][0]);
		assertEquals(map.get(9, 8), area[1][0]);
		assertNotNull(area[1][1]);
		assertEquals(map.get(9, 9), area[1][1]);
		assertNull(area[1][2]);
		assertNull(area[2][0]);
		assertNull(area[2][1]);
		assertNull(area[2][2]);
	}
}
