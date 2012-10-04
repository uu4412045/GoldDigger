package com.golddigger.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DirectionTest {

	@Test
	public void testParse() {
		assertEquals(Direction.NORTH, Direction.parse("north"));
		assertEquals(Direction.NORTH, Direction.parse("NORTH"));
		assertEquals(Direction.SOUTH, Direction.parse("south"));
		assertEquals(Direction.SOUTH, Direction.parse("SOUTH"));
		assertEquals(Direction.EAST, Direction.parse("east"));
		assertEquals(Direction.EAST, Direction.parse("EAST"));
		assertEquals(Direction.WEST, Direction.parse("west"));
		assertEquals(Direction.WEST, Direction.parse("WEST"));
		assertEquals(Direction.NORTH_EAST, Direction.parse("north_east"));
		assertEquals(Direction.NORTH_EAST, Direction.parse("NORTH_EAST"));
		assertEquals(Direction.NORTH_WEST, Direction.parse("north_west"));
		assertEquals(Direction.NORTH_WEST, Direction.parse("NORTH_WEST"));
		assertEquals(Direction.SOUTH_EAST, Direction.parse("south_east"));
		assertEquals(Direction.SOUTH_EAST, Direction.parse("SOUTH_EAST"));
		assertEquals(Direction.SOUTH_WEST, Direction.parse("south_west"));
		assertEquals(Direction.SOUTH_WEST, Direction.parse("SOUTH_WEST"));
		assertEquals(null, Direction.parse("lolwut!"));
	}

	@Test
	public void testIsHex() {
		assertTrue(Direction.NORTH.isHex());
		assertTrue(Direction.SOUTH.isHex());
		assertTrue(Direction.NORTH_EAST.isHex());
		assertTrue(Direction.NORTH_WEST.isHex());
		assertTrue(Direction.SOUTH_EAST.isHex());
		assertTrue(Direction.SOUTH_WEST.isHex());
		assertFalse(Direction.WEST.isHex());
		assertFalse(Direction.EAST.isHex());
	}

	@Test
	public void testGetOffset() {
		Coordinate point = new Coordinate(0,0);
		assertEquals(new Coordinate(-1,0), Direction.NORTH.getOffset(point));
		assertEquals(new Coordinate(1,0), Direction.SOUTH.getOffset(point));
		assertEquals(new Coordinate(0,-1), Direction.WEST.getOffset(point));
		assertEquals(new Coordinate(0,1), Direction.EAST.getOffset(point));
		assertEquals(new Coordinate(-1,1), Direction.NORTH_EAST.getOffset(point));
		assertEquals(new Coordinate(-1,-1), Direction.NORTH_WEST.getOffset(point));
		assertEquals(new Coordinate(0,1), Direction.SOUTH_EAST.getOffset(point));
		assertEquals(new Coordinate(0,-1), Direction.SOUTH_WEST.getOffset(point));
		
		point = new Coordinate(0,1);
		assertEquals(new Coordinate(0,2), Direction.NORTH_EAST.getOffset(point));
		assertEquals(new Coordinate(0,0), Direction.NORTH_WEST.getOffset(point));
		assertEquals(new Coordinate(1,2), Direction.SOUTH_EAST.getOffset(point));
		assertEquals(new Coordinate(1,0), Direction.SOUTH_WEST.getOffset(point));
	}

}
