package com.golddigger.utils;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

public class HexagonTest {

	@Test
	public void hexagonCenter(){
		Point2D point = new Hexagon(0,0).getCenter();
		assertEquals(0, point.getX(), 0.0001);
		assertEquals(0, point.getY(), 0.0001);

		point = new Hexagon(1,0).getCenter();
		assertEquals(Hexagon.HEIGHT, point.getX(), 0.0001);
		assertEquals(0, point.getY(), 0.0001);
		
		point = new Hexagon(1,2).getCenter();
		assertEquals(Hexagon.HEIGHT, point.getX(), 0.0001);
		assertEquals(3*Hexagon.RADIUS, point.getY(), 0.0001);
	}
	
	@Test
	public void offsetHexagonGetCenter(){
		Point2D center = new Hexagon(0,1).getCenter();
		assertEquals(Hexagon.HEIGHT/2, center.getX(), 0.0001);
		assertEquals(Hexagon.RADIUS*1.5, center.getY(), 0.0001);

		center = new Hexagon(1,1).getCenter();
		assertEquals(3*Hexagon.HEIGHT/2, center.getX(), 0.0001);
		assertEquals(Hexagon.RADIUS*1.5, center.getY(), 0.0001);

		center = new Hexagon(1,3).getCenter();
		assertEquals(3*Hexagon.HEIGHT/2, center.getX(), 0.0001);
		assertEquals(Hexagon.RADIUS*4.5, center.getY(), 0.0001);
	}
}
