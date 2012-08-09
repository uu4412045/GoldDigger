package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.core.AppContext;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;

public class GoldServiceTest {
	GenericServer server;
	TestingClient client;
	private static final String MAP = "wwwww\nw.2.w\nw.b.w\nw.9.ww\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new GenericServer();
		server.getContext().add(new TestGameTemplate(MAP));
		server.getContext().add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void testCollectingGold() {
		client.move(Direction.NORTH);
		// Test grabbing gold
		assertEquals("Should pickup all the gold","2",client.grab().trim());
		assertEquals("Should now be carrying 2 gold","2", client.carrying().trim());
		assertEquals("Should not be any gold on the ground","www\n...\n.b.", client.view().trim());
		client.move(Direction.SOUTH);
		client.move(Direction.SOUTH);
		// Test grab limit
		assertEquals("Should only pick up 1 gold", "1", client.grab().trim());
		assertEquals("Should not be able to carry any more gold", "FAILED", client.grab().trim());
		assertEquals("Should now be carrying 3 gold","3", client.carrying().trim());
		assertEquals("Should be 8 gold on the ground",".b.\n.8.\nwww", client.view().trim());
		assertEquals("Should only drop one gold", "1", client.drop().trim());
		assertEquals("Should now be carrying 2 gold","2", client.carrying().trim());
		assertEquals("Sould be 9 gold on the ground",".b.\n.9.\nwww", client.view().trim());
		client.grab();
		client.move(Direction.NORTH);
		assertEquals("Should drop all three", "3", client.drop().trim());
		assertEquals("Should be 0 gold on the base","...\n.b.\n.8.", client.view().trim());
		assertEquals("Should now have a score of 3", "3", client.score().trim());
		
	}

}
