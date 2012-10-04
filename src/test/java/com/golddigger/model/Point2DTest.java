package com.golddigger.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class Point2DTest {

	@Test
	public void testEquals() {
		Coordinate p1 = new Coordinate(10,10);
		Coordinate p2 = new Coordinate(10,10);

		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));
		
		p2 = new Coordinate(10,9);
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
	}
}
