package com.golddigger;

import static org.junit.Assert.*;

import org.junit.Test;

import static com.golddigger.core.Service.*;

public class TestPlugin {

	@Test
	public void testURLParsing() {
		String url = "/golddigger/digger/secret/move/south";
		assertEquals("digger",parseURL(url, URL_TARGET));
		assertEquals("secret",parseURL(url, URL_PLAYER));
		assertEquals("move",parseURL(url, URL_ACTION));
		assertEquals("south",parseURL(url, URL_EXTRA1));
		assertNull(parseURL(url, URL_EXTRA2));
		assertNull(parseURL(url, URL_EXTRA3));
	}

}
