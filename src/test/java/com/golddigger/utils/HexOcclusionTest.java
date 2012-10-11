package com.golddigger.utils;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

import com.golddigger.model.Coordinate;
import com.golddigger.utils.Hexagon;

public class HexOcclusionTest {
	private static final boolean X = true, o = false;
	
	
	@Test
	public void los2_North(){
		int[][] heightMap = {
				{1,1,1,1,1},
				{1,1,9,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1}
		};
		
		boolean[][] expected = {
				{o,o,X,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o}
		};
		
		assertArrayEquals(expected, HexOcclusion.create(heightMap, new Coordinate(2,2)));
	}
	
	@Test
	public void los2_South(){
		int[][] heightMap = {
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,9,1,1},
				{1,1,1,1,1}
		};
		
		boolean[][] expected = {
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,X,X,X,o}
		};
//		assertArrayEquals(expected, HexOcclusion.create(heightMap, 2));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, new Coordinate(2,2))));
	}
	
	@Test
	public void los2_NorthEast(){
		int[][] heightMap = {
				{1,1,1,1,1},
				{1,1,1,9,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1}
		};
		
		boolean[][] expected = {
				{o,o,o,o,X},
				{o,o,o,o,X},
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o}
		};
//		assertArrayEquals(expected, HexOcclusion.create(heightMap, 2));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, new Coordinate(2,2))));
	}
	

	@Test
	public void los2_SouthEast(){
		int[][] heightMap = {
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,9,1},
				{1,1,1,1,1},
				{1,1,1,1,1}
		};
		
		boolean[][] expected = {
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,X},
				{o,o,o,o,X}
		};
//		assertArrayEquals(expected, HexOcclusion.create(heightMap, 2));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, new Coordinate(2,2))));
	}
	
	public String v(boolean[][] map){
		String result = "";
		for (boolean[] row : map){
			for (boolean b : row){
				result += (b ? "X" : "o");
			}
			result += "\n";
		}
		return result;
	}
}