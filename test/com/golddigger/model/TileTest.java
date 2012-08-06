package com.golddigger.model;
import static org.junit.Assert.*;

import org.junit.Test;

import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.CityTile;
import com.golddigger.model.tiles.DeepWaterTile;
import com.golddigger.model.tiles.ForestTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.HillTile;
import com.golddigger.model.tiles.MountainTile;
import com.golddigger.model.tiles.RoadTile;
import com.golddigger.model.tiles.ShallowWaterTile;
import com.golddigger.model.tiles.TeleportTile;

public class TileTest {

	@Test
	public void testDefaultMovementCosts() {
		assertEquals(100, Tile.DEFAULT_MOVEMENT_COST);
		assertEquals(100, BaseTile.DEFAULT_MOVEMENT_COST);
		assertEquals(200, CityTile.DEFAULT_MOVEMENT_COST);
		assertEquals(500, DeepWaterTile.DEFAULT_MOVEMENT_COST);
		assertEquals(150, ShallowWaterTile.DEFAULT_MOVEMENT_COST);
		assertEquals(100, GoldTile.DEFAULT_MOVEMENT_COST);
		assertEquals(300, ForestTile.DEFAULT_MOVEMENT_COST);
		assertEquals(25,  RoadTile.DEFAULT_MOVEMENT_COST);
		assertEquals(500, MountainTile.DEFAULT_MOVEMENT_COST);
		assertEquals(100, TeleportTile.DEFAULT_MOVEMENT_COST);
		assertEquals(175, HillTile.DEFAULT_MOVEMENT_COST);
	}

}
