package com.golddigger.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class Point2DTest {

	@Test
	public void testEquals() {
		Point2D p1 = new Point2D(10,10);
		Point2D p2 = new Point2D(10,10);

		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));
		
		p2.lat = 9;
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
	}
}
