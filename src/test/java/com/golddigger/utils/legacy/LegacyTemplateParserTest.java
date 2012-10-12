package com.golddigger.utils.legacy;

import static org.junit.Assert.*;
import static com.golddigger.utils.LegacyTemplateParser.*;

import org.junit.Test;

import com.golddigger.model.Game;
import com.golddigger.model.Tile;
import com.golddigger.model.tiles.TeleportTile;
import com.golddigger.services.AdvTeleportService;
import com.golddigger.services.CannonService;
import com.golddigger.utils.LegacyTemplateParser;
import com.golddigger.utils.MapMaker;

public class LegacyTemplateParserTest {
	public static final String COST_STRING = DELIMITER+COSTS+"\n"+"b=150\n9=900\nm=230";
	public static final String LINE_OF_SIGHT_STRING = DELIMITER+LINE_OF_SIGHT+SEPERATOR+"100";
	public static final String TELEPORT_STRING_EXPECTED = DELIMITER
			+ DIS_TELEPORTS + "\n" + " 2,3->4,5\n6,7->8,9";

	public static final String TELEPORT_STRING_EMPTY = DELIMITER + DIS_TELEPORTS;

	@Test
	public void testParseCosts() {
		String[] costs = parseCosts(COST_STRING);
		assertEquals("b=150", costs[0]);
		assertEquals("9=900", costs[1]);
		assertEquals("m=230", costs[2]);
	}

	@Test
	public void testGetLineOfSight() {
		assertEquals(100, getLineOfSight(LINE_OF_SIGHT_STRING));
	}

	@Test
	public void cannonsEnabled(){
		String field = DELIMITER+CANNON+"\n"+DELIMITER+TILES+"\nwww\nwbw\nwww";
		Game game = LegacyTemplateParser.parse(field).build();
		assertEquals(1, game.getServices(CannonService.class).size());
	}
	
	@Test
	public void cannonsNotEnabled(){
		String field = buildSection(TILES, "www\nwbw\nwww");
		Game game = LegacyTemplateParser.parse(field).build();
		assertEquals(0, game.getServices(CannonService.class).size());
	}
	
	@Test
	public void testParseDTeleportsExpected() {
		String[] parsed = LegacyTemplateParser.parseDTeleports(TELEPORT_STRING_EXPECTED);
		String[] teleports = { "2,3->4,5", "6,7->8,9" };

		assertArrayEquals(teleports, parsed);
	}

	@Test
	public void testParseDTeleportsEmpty() {
		String[] parsed = LegacyTemplateParser.parseDTeleports(TELEPORT_STRING_EMPTY);
		String[] teleports = { "" };

		assertArrayEquals(teleports, parsed);
	}
	
	@Test
	public void testParseAdvantageTeleports(){
		String map = "wwwwww\nw.b..w\nwwwwww";
		String field = LegacyTemplateParser.buildSection(ADV_TELEPORTS, "1,1 -> 1,3");
		field += LegacyTemplateParser.buildSection(TILES, map);
		Game game = LegacyTemplateParser.parse(field).build();
		
		assertEquals(1, game.getServices(AdvTeleportService.class).size());

		System.out.println(MapMaker.parse(game.getMap()));
		
		Tile tile1 = game.getMap().get(1,1);
		Tile tile2 = game.getMap().get(1,3);
		assertTrue(tile1 instanceof TeleportTile);
		assertTrue(tile2 instanceof TeleportTile);
		TeleportTile teleport1 = (TeleportTile) tile1;
		TeleportTile teleport2 = (TeleportTile) tile2;

		assertEquals(game.getMap().getPosition(tile2), teleport1.getDestinationPos());
		assertEquals(game.getMap().getPosition(tile1), teleport2.getDestinationPos());
	}
}
