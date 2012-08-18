package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.ServletServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.templates.CustomizableGameTemplate;

public class LOSHexViewServiceTest {
	ServletServer server;
	TestingClient client;
	private static final String MAP = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		template.setMap(MAP);
		template.setNumberOfSides(6);
		template.setLineOfSight(2);
		
		server = new ServletServer();
		server.add(template);
		server.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void test() {
		// starts at the base at position (2, 2)		
		assertEquals("??w??\nw...w\nw.b.w\nw...w\n?www?", client.view().trim());
		client.move(Direction.SOUTH);
		assertEquals("??.??\nw.b.w\nw...w\nwwwww\n-----", client.view().trim());
		client.move(Direction.NORTH_EAST);
		assertEquals("?..w-\n.b.w-\n...w-\nwwww-\n-----", client.view().trim());
		client.move(Direction.NORTH);
		assertEquals("?www-\n...w-\n.b.w-\n...w-\n??w?-", client.view().trim());		
		client.move(Direction.NORTH);
		assertEquals("-----\nwwww-\n...w-\n.b.w-\n??.?-", client.view().trim());
		client.move(Direction.SOUTH_WEST);
		assertEquals("-----\nwwwww\nw...w\nw.b.w\n?...?", client.view().trim());
		client.move(Direction.NORTH_WEST);
		assertEquals("-----\n-wwww\n-w...\n-w.b.\n-?.??", client.view().trim());
		client.move(Direction.SOUTH);
		assertEquals("-www?\n-w...\n-w.b.\n-w...\n-?w??", client.view().trim());
		client.move(Direction.SOUTH);
		assertEquals("-w..?\n-w.b.\n-w...\n-wwww\n-----", client.view().trim());

	}

}
