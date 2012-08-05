package com.golddigger.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.golddigger.model.Map;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.WallTile;

public class MapMakerTest {
	private static final String STRING_MAP = "wwwww\nw.1.w\nw.b.w\nw.9.w\nwwwww";
	
	@Test
	public void testParse() {
		Map map = MapMaker.parse(STRING_MAP);
		assertTrue(map.get(0, 0) instanceof WallTile);
		assertTrue(map.get(0, 4) instanceof WallTile);
		assertTrue(map.get(4, 0) instanceof WallTile);
		assertTrue(map.get(4, 4) instanceof WallTile);

		assertTrue(map.get(2, 2) instanceof BaseTile);

		assertTrue(map.get(1, 1) instanceof GoldTile);
		assertTrue(map.get(1, 2) instanceof GoldTile);
		assertTrue(map.get(3, 2) instanceof GoldTile);
		
	}

}
