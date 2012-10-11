package com.golddigger.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.golddigger.model.Coordinate;

public class OcclusionTools {
	private static final boolean X = true, o = false;

	@Test
	public void north() {
		int[][] heightMap = heightmap(new Coordinate(1, 2));
		boolean[][] expected = boolmap(new Coordinate(0,2));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,2))));
	}
	
	@Test
	public void north_east() {
		int[][] heightMap = heightmap(new Coordinate(1, 3));
		boolean[][] expected = boolmap(p(0,4),p(1,4));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,2))));
	}
	
	@Test
	public void north_west() {
		int[][] heightMap = heightmap(new Coordinate(1, 1));
		boolean[][] expected = boolmap(p(0,0),p(1,0));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,2))));
	}

	@Test
	public void south(){
		int[][] heightMap = heightmap(new Coordinate(3, 2));
		boolean[][] expected = boolmap(p(4,1), p(4,2), p(4,3));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,2))));
	}

	@Test
	public void south_east(){
		int[][] heightMap = heightmap(new Coordinate(2, 3));
		boolean[][] expected = boolmap(p(3,4), p(4,4));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,2))));
	}

	@Test
	public void south_west(){
		int[][] heightMap = heightmap(new Coordinate(2, 1));
		boolean[][] expected = boolmap(p(3,0), p(4,0));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,2))));
	}

	/* ******************** *\
	 *   OFFSET POSITIONS   *
	\********************** */
	
	@Test
	public void north_offset(){
		int[][] heightMap = heightmap(new Coordinate(1, 2));
		boolean[][] expected = boolmap(p(0,1), p(0,2), p(0,3));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,3))));
	}	
	
	@Test
	public void north_east_offset(){
		int[][] heightMap = heightmap(new Coordinate(2, 3));
		boolean[][] expected = boolmap(p(0,4), p(1,4));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,3))));
	}	
	@Test
	public void north_west_offset(){
		int[][] heightMap = heightmap(new Coordinate(2, 1));
		boolean[][] expected = boolmap(p(0,0), p(1,0));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,3))));
	}
	
	@Test
	public void south_offset(){
		int[][] heightMap = heightmap(new Coordinate(3, 2));
		boolean[][] expected = boolmap(new Coordinate(4,2));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,3))));
	}
	
	@Test
	public void south_west_offset(){
		int[][] heightMap = heightmap(new Coordinate(3, 1));
		boolean[][] expected = boolmap(p(3,0), p(4,0));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,3))));
	}
	
	@Test
	public void south_east_offset(){
		int[][] heightMap = heightmap(new Coordinate(3, 3));
		boolean[][] expected = boolmap(p(3,4), p(4,4));
		assertEquals(v(expected), v(HexOcclusion.create(heightMap, p(2,3))));
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
	
	private boolean[][] boolmap(Coordinate... points){
		boolean[][] expected = {
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o},
				{o,o,o,o,o}
		};
		for (Coordinate cood : points){
			expected[cood.lat][cood.lng] = X;
		}
		return expected;
	}
	
	private int[][] heightmap(Coordinate... points){
		int[][] heightMap = {
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1}
		};
		
		for (Coordinate cood : points){
			heightMap[cood.lat][cood.lng] = 9;
		}
		return heightMap;
	}
	
	private Coordinate p(int lat, int lng){
		return new Coordinate(lat, lng);
	}
}
