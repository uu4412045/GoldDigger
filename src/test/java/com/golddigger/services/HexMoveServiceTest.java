package com.golddigger.services;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.templates.CustomizableGameTemplate;
public class HexMoveServiceTest {
	GenericServer server;
	TestingClient client;
	private static final String MAP = 
			"wwwwww\n"+
			"wb...w\n"+
			"w....w\n"+
			"w....w\n"+
			"w....w\n"+
			"wwwwww";

	@Before
	public void before() {
		server = new GenericServer();
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		template.setMap(MAP);
		template.setNumberOfSides(6);
		server.addTemplate(template);
		server.addPlayer("test1","secret");
		client = new TestingClient("test1", "http://localhost:8066");
		client.move(Direction.SOUTH);
		client.move(Direction.NORTH_EAST);
		client.move(Direction.SOUTH_EAST);
//        assertEquals( "?.?\n...\n...\n", client.view());
	}
	
	@After
	public void after(){
		server.stop();
	}

	@Test
	public void canMoveInAllDirections() throws Exception {
		moveAndAssert(Direction.NORTH, "?w?\n...\n...\n");
		moveAndAssert(Direction.SOUTH_EAST, "..w\n..w\n?.?\n");
		moveAndAssert(Direction.SOUTH, "..w\n..w\n?.?\n");
		moveAndAssert(Direction.SOUTH, "..w\n..w\n?w?\n");
		moveAndAssert(Direction.SOUTH_WEST, "?.?\n...\nwww\n");
		moveAndAssert(Direction.NORTH_WEST, "...\n...\n?w?\n");
		moveAndAssert(Direction.NORTH_EAST, "?.?\n...\n...\n");
	}

	@Test
	public void wontMoveOutsideNorthBounds() throws Exception {
		moveAndAssert(Direction.NORTH, "?w?\n...\n...\n");
		moveAndAssert(Direction.NORTH, "?w?\n...\n...\n");
	}

	@Test
	public void wontMoveOutsideSouthBounds() throws Exception {    	        
		moveAndAssert(Direction.SOUTH, "?.?\n...\n...\n");
		moveAndAssert(Direction.SOUTH, "?.?\n...\nwww\n");
		moveAndAssert(Direction.SOUTH, "?.?\n...\nwww\n");        
	}

	@Test
	public void wontMoveOutsideNorthEastBounds() throws Exception {
		moveAndAssert(Direction.NORTH_EAST, "..w\n..w\n?.?\n");
		moveAndAssert(Direction.NORTH_EAST, "..w\n..w\n?.?\n");
	}

	@Test
	public void wontMoveOutsideSouthWestBounds() throws Exception {
		moveAndAssert(Direction.SOUTH_WEST, "...\n...\n?.?\n");
		moveAndAssert(Direction.SOUTH_WEST, "?.?\nw..\nw..\n");
		moveAndAssert(Direction.SOUTH_WEST, "?.?\nw..\nw..\n");

	}

	@Test
	public void wontMoveOutsideSouthEastBounds() throws Exception {
		moveAndAssert(Direction.SOUTH_EAST, "..w\n..w\n?.?\n");       
		moveAndAssert(Direction.SOUTH_EAST, "..w\n..w\n?.?\n");
	}
	@Test
	public void wontMoveOutsideNorthWestBounds() throws Exception {
		moveAndAssert(Direction.NORTH_WEST, "b..\n...\n?.?\n");        
		moveAndAssert(Direction.NORTH_WEST, "?w?\nwb.\nw..\n");        
		moveAndAssert(Direction.NORTH_WEST, "?w?\nwb.\nw..\n");
	}

	private void moveAndAssert(Direction move, String expected) {
		client.move(move);        
		assertEquals(expected.trim(), client.view().trim());
	}

}
