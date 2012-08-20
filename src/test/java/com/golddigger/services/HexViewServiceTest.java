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

public class HexViewServiceTest {
	ServletServer server;
	TestingClient client;
	private static final String MAP = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		template.setMap(MAP);
		template.setNumberOfSides(6);
		template.setLineOfSight(1);
		
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
		assertEquals("...\n.b.\n?.?", client.view().trim());
		client.move(Direction.NORTH);
		assertEquals("www\n...\n?b?", client.view().trim());
		client.move(Direction.SOUTH_WEST);
		assertEquals("?w?\nw..\nw.b", client.view().trim());
		client.move(Direction.SOUTH);
		assertEquals("?.?\nw.b\nw..", client.view().trim());
		client.move(Direction.SOUTH);
		assertEquals("?.?\nw..\nwww", client.view().trim());
		client.move(Direction.NORTH_EAST);
		assertEquals(".b.\n...\n?w?", client.view().trim());
		client.move(Direction.SOUTH_EAST);
		assertEquals("?.?\n..w\nwww", client.view().trim());
		client.move(Direction.NORTH);
		assertEquals( "?.?\nb.w\n..w", client.view().trim());
		client.move(Direction.NORTH);
		assertEquals("?w?\n..w\nb.w", client.view().trim());
		
	}

}
