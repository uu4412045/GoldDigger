package com.golddigger.utils.legacy;

import static org.junit.Assert.*;
import static com.golddigger.utils.legacy.LegacyTemplateParser.*;

import org.junit.Test;

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

}
