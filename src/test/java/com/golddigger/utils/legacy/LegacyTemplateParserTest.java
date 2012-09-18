package com.golddigger.utils.legacy;

import static org.junit.Assert.*;
import static com.golddigger.utils.LegacyTemplateParser.*;

import org.junit.Test;

import com.golddigger.model.Game;
import com.golddigger.services.CannonService;
import com.golddigger.templates.GameTemplate;
import com.golddigger.utils.LegacyTemplateParser;

public class LegacyTemplateParserTest {
	public static final String COST_STRING = DELIMITER+COSTS+"\n"+"b=150\n9=900\nm=230";
	public static final String LINE_OF_SIGHT_STRING = DELIMITER+LINE_OF_SIGHT+SEPERATOR+"100";
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
		String field = DELIMITER+TILES+"\nwww\nwbw\nwww";
		Game game = LegacyTemplateParser.parse(field).build();
		assertEquals(0, game.getServices(CannonService.class).size());
	}
}
