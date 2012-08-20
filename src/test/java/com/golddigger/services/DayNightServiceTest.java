package com.golddigger.services;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.ServletServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.services.generators.ServiceGenerator;
import com.golddigger.templates.TestGameTemplate;

public class DayNightServiceTest {
	ServletServer server;
	TestingClient client;
	private static final String MAP = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new ServletServer();
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void testSquareField() {
		ServiceGenerator gen = new ServiceGenerator(){
			@Override
			public GameService[] generate() {
				return new GameService[]{
						new ViewService(4),
						new SquareMoveService(),
						new DayNightService(3,50)};
			}
		};
		server.add(new TestGameTemplate(MAP, gen));
		server.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
		
		Player player = server.getPlayer("test");
		Game game = server.getGame(player);
		List<ViewService> services = game.getServices(ViewService.class);
		assertEquals(1, services.size());

		ViewService vService;
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
	
	@Test
	public void testHexField() {
		ServiceGenerator gen = new ServiceGenerator(){
			@Override
			public GameService[] generate() {
				return new GameService[]{
						new HexViewService(4),
						new HexMoveService(),
						new DayNightService(3,50)};
			}
		};
		server.add(new TestGameTemplate(MAP, gen));
		server.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
		
		Player player = server.getPlayer("test");
		Game game = server.getGame(player);
		List<HexViewService> services = game.getServices(HexViewService.class);
		assertEquals(1, services.size());
		
		HexViewService vService;
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
