package com.golddigger.services;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.templates.CustomizableGameTemplate;

public class DayNightServiceTest {
	GenericServer server;
	TestingClient client;
	CustomizableGameTemplate template;
	private final String name = "test1";

	@Before()
	public void setup(){
		server = new GenericServer();
		client = new TestingClient(name, "http://localhost:8066");
		template = new CustomizableGameTemplate();
		template.setMap("wwwww\nw...w\nw.b.w\nw...w\nwwwww");
		template.setDayNight(3, 50);
		template.setLineOfSight(4);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void testSquareField() {
		server.addTemplate(template);
		server.addPlayer(name, "secret");
		Player player = server.getMain().getPlayer(name);
		Game game = server.getMain().getGame(player);
		List<ViewService> services = game.getServices(ViewService.class);
		List<DayNightService> dayNightServices = game.getServices(DayNightService.class);
		assertEquals("There is no view service set",1, services.size());
		assertEquals("There is no day night service set", 1, dayNightServices.size());

		ViewService vService = services.get(0);
		assertNotNull(vService);
		DayNightService dnService = dayNightServices.get(0);
		assertNotNull(dnService);
		assertTrue("Was not initialised to day time",dnService.isDay());
		
		assertEquals("LOS didn't start off at the original value", 4, vService.getLineOfSight());
		client.move(Direction.NORTH);
		client.move(Direction.NORTH);
		client.move(Direction.SOUTH);
		assertFalse("Should have been changed to night time, but wasnt", dnService.isDay());
		assertEquals("LOS wasn't scaled ", 2, vService.getLineOfSight());
		client.move(Direction.NORTH);
		client.move(Direction.SOUTH);
		client.move(Direction.NORTH);
		assertTrue("Should have been changed to day time, but wasnt", dnService.isDay());
		assertEquals("LOS wasn't scaled back to the original Value", 4, vService.getLineOfSight());
		
		int i = dnService.getTurnCount();
		client.move(Direction.SOUTH_EAST);
		client.move(Direction.SOUTH_WEST);
		client.move(Direction.NORTH_EAST);
		client.move(Direction.NORTH_EAST);
		assertEquals("invalid moves shouldn't increase the turn count", i, dnService.getTurnCount());
	}
	
	@Test
	public void testHexField() {
		template.setNumberOfSides(6);
		server.addTemplate(template);
		server.addPlayer(name, "secret");
		Player player = server.getMain().getPlayer(name);
		Game game = server.getMain().getGame(player);
		List<HexViewService> services = game.getServices(HexViewService.class);
		List<DayNightService> dayNightServices = game.getServices(DayNightService.class);
		assertEquals(1, services.size());
		assertEquals("There is no day night service set", 1, dayNightServices.size());
		
		HexViewService vService;
		vService = services.get(0);
		assertNotNull(vService);
		DayNightService dnService = dayNightServices.get(0);
		assertNotNull(dnService);
		assertTrue("Was not initialised to day time",dnService.isDay());
		
		assertEquals(4, vService.getLineOfSight());
		client.move(Direction.NORTH);
		client.move(Direction.NORTH);
		client.move(Direction.SOUTH);
		assertEquals(2, vService.getLineOfSight());
		client.move(Direction.NORTH);
		client.move(Direction.SOUTH);
		client.move(Direction.NORTH);
		assertEquals(4, vService.getLineOfSight());
		

		int i = dnService.getTurnCount();
		client.move(Direction.EAST);
		client.move(Direction.WEST);
		assertEquals("invalid moves shouldn't increase the turn count", i, dnService.getTurnCount());
	}
}
