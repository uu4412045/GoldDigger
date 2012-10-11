package com.golddigger.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.MountainTile;
import com.golddigger.model.tiles.OccludedTile;

public class JsonEncoderTest {
	
	@Test
	public void testEncodeTile() {
		String actual = JsonEncoder.encodeTile(new MountainTile());
		String expected = "{\"tile\":{\"type\":\"m\"}}";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEncodeNullTile(){
		String actual = JsonEncoder.encodeTile(null);
		String expected = "{\"tile\":{\"type\":\"out-of-bounds\"}}";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEncodeOccludedTile(){
		String actual = JsonEncoder.encodeTile(new OccludedTile());
		String expected = "{\"tile\":{\"type\":\"unknown\"}}";
		assertEquals(expected, actual);
	}

	@Test
	public void testEncodeUnit() {
		String name = "test";
		Unit unit = new Unit(new Player(name, "secret"), new Coordinate(1,1));
		String actual = JsonEncoder.encodeUnit(unit, new Coordinate(2,2));
		String expected = "{\"digger\":{\"position\":\""+new Coordinate(-1,-1).toString()+"\",\"owner\":\""+name+"\"}}";
		assertEquals(expected, actual);
	}

	@Test
	public void testEncodeArea(){
		MountainTile tile = new MountainTile();
		String expected = "\"tiles\":[["+JsonEncoder.encodeTile(tile)+"]]";
		String actual = JsonEncoder.encodeArea(new Tile[][]{{tile}});
		assertEquals(expected, actual);
	}
	
	
}
