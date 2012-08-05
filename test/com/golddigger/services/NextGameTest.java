package com.golddigger.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.TestServer;
import com.golddigger.client.TestingClient;
import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Map;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.MapMaker;

public class NextGameTest {
	TestServer server;
	TestingClient client;
	private static final String STRING_MAP_1 = "wwwww\nw.2.w\nw.b.w\nw...ww\nwwwww";
	private static final String STRING_MAP_2 = "www\nwbw\nwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		Map map_1 = MapMaker.parse(STRING_MAP_1);
		Map map_2 = MapMaker.parse(STRING_MAP_2);
		List<Service> services = new ArrayList<Service>();
		services.add(new MoveService());
		services.add(new ViewService());
		services.add(new GrabService());
		services.add(new DropService());
		services.add(new ScoreService());
		services.add(new CarryingService());
		services.add(new NextService());
		server = new TestServer();
		AppContext.add(new TestGameTemplate(map_1, services));
		AppContext.add(new TestGameTemplate(map_2, services));
		AppContext.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void test() {
		assertEquals("Should be on the first map", ".2.\n.b.\n...", client.view().trim());
		assertEquals("Still gold on the field","FAILED", client.next().trim());
		client.move(Direction.NORTH);
		client.grab();
		assertEquals("Still holding the gold","FAILED", client.next().trim());
		client.move(Direction.SOUTH);
		client.drop();
		assertEquals("Should have been allowed to progress","OK", client.next().trim());
		assertEquals("Should be on the next map", "www\nwbw\nwww", client.view().trim());
		
	}

}
