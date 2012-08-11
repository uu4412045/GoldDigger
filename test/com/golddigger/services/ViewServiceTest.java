package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.generators.BaseServiceGenerator;

public class ViewServiceTest {
	GenericServer server;
	TestingClient client;
	private static final String MAP_1 = "wwwwww\nw.123w\nw45b6w\nw789ww\nwwwww";
	private static final String MAP_2 = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		BaseServiceGenerator gen = new BaseServiceGenerator();
		gen.setLOS(2);
		server = new GenericServer();
		client = new TestingClient("test", BASE_URL);
		server.add(new TestGameTemplate(MAP_1));
		server.add(new TestGameTemplate(MAP_2, gen));
		server.add(new Player("test", "secret"));
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void testView() {
		assertEquals("123\n5b6\n89w",client.view().trim());
		assertEquals("OK",client.move(Direction.WEST).trim());
		assertEquals(".12\n45b\n789",client.view().trim());
		server.progress(server.getPlayer("test"));
		
		//Testing Line Of Sight
		assertEquals("wwwww\nw...w\nw.b.w\nw...w\nwwwww",client.view().trim());
	}
}
