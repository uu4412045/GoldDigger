package com.golddigger.services;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.core.GameService;
import com.golddigger.model.Game;
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
			public GameService[] generate() {
				return new GameService[]{
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
		Player player = server.getContext().getPlayer("test");
		Game game = server.getContext().getGame(player);
		List<ViewService> services = game.getServices(ViewService.class);
		assertEquals(1, services.size());
		vService = services.get(0);
		assertNotNull(vService);
		
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
