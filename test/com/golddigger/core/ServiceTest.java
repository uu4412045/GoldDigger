package com.golddigger.core;

import static com.golddigger.core.Service.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ServiceTest {

	@Test
	public void test() {
		String url = "localhost:8066/golddigger/digger/test/move/south";
		
		assertEquals("localhost:8066", parseURL(url, URL_HOST));
		assertEquals("golddigger", parseURL(url, URL_CONTEXT));
		assertEquals("digger",parseURL(url, URL_TARGET));
		assertEquals("test",parseURL(url, URL_PLAYER));
		assertEquals("move",parseURL(url, URL_ACTION));
		assertEquals("south",parseURL(url, URL_EXTRA1));
		assertNull(parseURL(url, URL_EXTRA2));
		assertNull(parseURL(url, URL_EXTRA3));
	}

}
