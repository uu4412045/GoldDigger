package com.golddigger.templates;

import static com.golddigger.utils.LegacyTemplateParser.ADV_TELEPORTS;
import static com.golddigger.utils.LegacyTemplateParser.TILES;
import static org.junit.Assert.*;

import org.junit.Test;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.model.Tile;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.TeleportTile;
import com.golddigger.services.AdvTeleportService;
import com.golddigger.utils.LegacyTemplateParser;

public class AdvantageousTeleportationConfigTest {
	private final String map =
				"wwwwwwwww\n"+
				"wb....w.w\n"+
				"wwwwwww.w\n"+
				"w.......w\n"+
				"wwwwwwwww";
	private String field = LegacyTemplateParser.buildSection(TILES, map);
	
	@Test
	public void successfulTest() {
		field += LegacyTemplateParser.buildSection(ADV_TELEPORTS, "1,2 -> 1,4");
		Game game = LegacyTemplateParser.parse(field).build();
		
		assertEquals(1, game.getServices(AdvTeleportService.class).size());
		assertTeleportPair(new Coordinate(1,2), new Coordinate(1,4), game.getMap());
	}
	
	@Test
	public void unreachable_src(){
		field += LegacyTemplateParser.buildSection(ADV_TELEPORTS, "3,1 -> 1,3");
		Game game = LegacyTemplateParser.parse(field).build();
		
		assertEquals(1, game.getServices(AdvTeleportService.class).size());
		assertTeleportPair(new Coordinate(3,1), new Coordinate(1,3), game.getMap());
	}
	
	@Test
	public void unreachable_dst(){
		field += LegacyTemplateParser.buildSection(ADV_TELEPORTS, "1,2 -> 3,3");
		Game game = LegacyTemplateParser.parse(field).build();
		
		assertEquals(1, game.getServices(AdvTeleportService.class).size());
		assertTeleportPair(new Coordinate(1,2), new Coordinate(3,3), game.getMap());
	}
	
	@Test
	public void unreachable_src_and_dst(){
		field += LegacyTemplateParser.buildSection(ADV_TELEPORTS, "3,3 -> 3,5");
		Game game = LegacyTemplateParser.parse(field).build();
		Tile tile1 = game.getMap().get(3,3);
		Tile tile2 = game.getMap().get(3,5);
		assertFalse(tile1 instanceof TeleportTile);
		assertFalse(tile2 instanceof TeleportTile);
	}
	
	@Test
	public void src_is_base(){
		field += LegacyTemplateParser.buildSection(ADV_TELEPORTS, "1,1 -> 3,5");
		Game game = LegacyTemplateParser.parse(field).build();
		assertTrue(game.getMap().get(1,1) instanceof BaseTile);
		assertFalse(game.getMap().get(3,5) instanceof TeleportTile);
	}
	
	@Test
	public void dst_is_base(){
		field += LegacyTemplateParser.buildSection(ADV_TELEPORTS, "3,5 -> 1,1");
		Game game = LegacyTemplateParser.parse(field).build();
		assertFalse(game.getMap().get(3,5) instanceof TeleportTile);
		assertTrue(game.getMap().get(1,1) instanceof BaseTile);
	}

	public static void assertTeleportPair(Coordinate src, Coordinate dst, Map map){
		Tile tile1 = map.get(src);
		Tile tile2 = map.get(dst);
		assertTrue(tile1 instanceof TeleportTile);
		assertTrue(tile2 instanceof TeleportTile);
		TeleportTile teleport1 = (TeleportTile) tile1;
		TeleportTile teleport2 = (TeleportTile) tile2;

		assertEquals(dst, teleport1.getDestinationPos());
		assertEquals(src, teleport2.getDestinationPos());
	}
}
