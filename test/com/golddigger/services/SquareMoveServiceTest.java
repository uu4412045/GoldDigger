package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.templates.TestGameTemplate;

public class SquareMoveServiceTest {
	GenericServer server;
	TestingClient client;
	private static final String MAP = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new GenericServer();
		server.add(new TestGameTemplate(MAP));
		server.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}

	@Test
	public void testInvalidMoves(){
		//Make sure invalid directions fail
		moveAndAssert(Direction.NORTH_EAST, "...\n.b.\n...\n", false);
		moveAndAssert(Direction.NORTH_WEST, "...\n.b.\n...\n", false);
		moveAndAssert(Direction.SOUTH_EAST, "...\n.b.\n...\n", false);
		moveAndAssert(Direction.SOUTH_WEST, "...\n.b.\n...\n", false);
	}
	
	@Test
	public void testValidMoves(){
		// Testing valid moves
		moveAndAssert(Direction.NORTH, "www\n...\n.b.\n");
		moveAndAssert(Direction.EAST, "www\n..w\nb.w\n");
		moveAndAssert(Direction.SOUTH, "..w\nb.w\n..w\n");
		moveAndAssert(Direction.WEST, "...\n.b.\n...\n");
	}
	
	@Test
	public void testMapBounds() throws Exception {
		//Should't move outside north bounds
		moveAndAssert(Direction.NORTH, "www\n...\n.b.\n");
		moveAndAssert(Direction.NORTH, "www\n...\n.b.\n", false);
		
		//Shouldn't move outside south bounds
		moveAndAssert(Direction.SOUTH, "...\n.b.\n...\n");
		moveAndAssert(Direction.SOUTH, ".b.\n...\nwww\n");
		moveAndAssert(Direction.SOUTH, ".b.\n...\nwww\n",false);
		
		//Shouldn't move outside east bounds
		moveAndAssert(Direction.NORTH, "...\n.b.\n...\n");
		moveAndAssert(Direction.EAST, "..w\nb.w\n..w\n");
		moveAndAssert(Direction.EAST, "..w\nb.w\n..w\n", false);
		
		//Shouldn't move outside west bounds
		moveAndAssert(Direction.WEST, "...\n.b.\n...\n");
		moveAndAssert(Direction.WEST, "w..\nw.b\nw..\n");
		moveAndAssert(Direction.WEST, "w..\nw.b\nw..\n",false);
	}
	
	private void moveAndAssert(Direction d, String expected){moveAndAssert(d, expected, true);}
	private void moveAndAssert(Direction d, String expected, boolean success) {
		assertEquals((success ? "OK" : "FAILED"), client.move(d).trim());
		assertEquals(expected, client.view());
	}
}
