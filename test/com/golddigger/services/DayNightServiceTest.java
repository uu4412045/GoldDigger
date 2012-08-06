package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.TestServer;
import com.golddigger.client.TestingClient;
import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.core.ServiceGenerator;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;

public class DayNightServiceTest {
	TestServer server;
	TestingClient client;
	ViewService vService;
	private static final String MAP = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new TestServer();
		ServiceGenerator gen = new ServiceGenerator(){
			@Override
			public Service[] generate() {
				return new Service[]{
						new ViewService(4),
						new MoveService(),
						new DayNightService(3,50)};
			}
		};
		AppContext.add(new TestGameTemplate(MAP, gen));
		AppContext.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void testLineOfSightChanges() {
		for (Service service : AppContext.getGame(AppContext.getPlayer("test")).getServices()){
			if (service instanceof ViewService) vService = (ViewService) service;
		}
		assertEquals(4, vService.getLineOfSight());
		client.move(Direction.NORTH);
		client.move(Direction.NORTH);
		client.move(Direction.SOUTH);
		assertEquals(2, vService.getLineOfSight());
		client.move(Direction.NORTH);
		client.move(Direction.SOUTH);
		client.move(Direction.NORTH);
		assertEquals(4, vService.getLineOfSight());
	}

}
