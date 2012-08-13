package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.ServletServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Player;
import com.golddigger.templates.CustomizableGameTemplate;

public class HexMoveServiceTest {
	ServletServer server;
	TestingClient client;
	private static final String MAP = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		template.setMap(MAP);
		template.setNumberOfSides(6);
		
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
//		fail("Not yet implemented");
	}

}
