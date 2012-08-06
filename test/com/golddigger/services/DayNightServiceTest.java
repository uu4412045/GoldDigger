package com.golddigger.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.CityTile;
import com.golddigger.model.tiles.DeepWaterTile;
import com.golddigger.model.tiles.ForestTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.HillTile;
import com.golddigger.model.tiles.MountainTile;
import com.golddigger.model.tiles.RoadTile;
import com.golddigger.model.tiles.ShallowWaterTile;
import com.golddigger.model.tiles.TeleportTile;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.MapMaker;

public class DayNightServiceTest {
	TestServer server;
	TestingClient client;
	ViewService vService;
	private static final String STRING_MAP_1 = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new TestServer();
		Map map = MapMaker.parse(STRING_MAP_1);
		List<Service> services = new ArrayList<Service>();
		vService = new ViewService(4);
		services.add(new MoveService());
		services.add(new DayNightService(3, 50));
		services.add(vService);
		AppContext.add(new TestGameTemplate(map, services));
		AppContext.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void test() {
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
