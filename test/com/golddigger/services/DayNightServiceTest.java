package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.core.Service;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.generators.ServiceGenerator;

public class DayNightServiceTest {
	GenericServer server;
	TestingClient client;
	ViewService vService;
	private static final String MAP = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new GenericServer();
		ServiceGenerator gen = new ServiceGenerator(){
			@Override
			public Service[] generate() {
				return new Service[]{
						new ViewService(4),
						new MoveService(),
						new DayNightService(3,50)};
			}
		};
		server.getContext().add(new TestGameTemplate(MAP, gen));
		server.getContext().add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void testLineOfSightChanges() {
		for (Service service : server.getContext().getGame(server.getContext().getPlayer("test")).getServices()){
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
