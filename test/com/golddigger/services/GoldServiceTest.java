package com.golddigger.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.golddigger.TestServer;
import com.golddigger.client.TestingClient;
import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Map;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.utils.MapMaker;
import com.meterware.httpunit.WebConversation;

public class GoldServiceTest {
	TestServer server;
	TestingClient client;
	private static final String STRING_MAP = "wwwww\nw.2.w\nw.b.w\nw.9.ww\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		Map map = MapMaker.parse(STRING_MAP);
		List<Service> services = new ArrayList<Service>();
		services.add(new MoveService());
		services.add(new ViewService());
		services.add(new GrabService());
		services.add(new DropService());
		services.add(new ScoreService());
		services.add(new CarryingService());
		server = new TestServer(map, services);
		AppContext.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void testCollectingGold() {
		client.move(Direction.NORTH);
		assertEquals("Should pickup all the gold","2",client.grab().trim());
		assertEquals("Should now be carrying 2 gold","2", client.carrying().trim());
		assertEquals("Should not be any gold on the ground","www\n...\n.b.", client.view().trim());
		client.move(Direction.SOUTH);
		client.move(Direction.SOUTH);
		assertEquals("Should only pick up 1 gold", "1", client.grab().trim());
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
