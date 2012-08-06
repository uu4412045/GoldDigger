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
import com.golddigger.model.tiles.*;
import com.golddigger.services.MoveService;
import com.golddigger.services.ViewService;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.MapMaker;

import static com.golddigger.services.MoveService.*;


public class MovementCostTest {
	TestServer server;
	TestingClient client;
	private static final String STRING_MAP_1 = "wwwwwwwwwwwwww\nwbbcdshfrmt.19w\nwwwwwwwwwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new TestServer();
		// adding GameTemplate with default costs
		Map map = MapMaker.parse(STRING_MAP_1);
		List<Service> services1 = new ArrayList<Service>();
		services1.add(new MoveService());
		services1.add(new ViewService());
		services1.add(new NextService());
		AppContext.add(new TestGameTemplate(map, services1));
		
		// Adding GameTemplate with custom costs
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put(BaseTile.class.getSimpleName(),50);
		costs.put(CityTile.class.getSimpleName(),50);
		costs.put(DeepWaterTile.class.getSimpleName(),50);
		costs.put(ShallowWaterTile.class.getSimpleName(),50);
		costs.put(HillTile.class.getSimpleName(),50);
		costs.put(ForestTile.class.getSimpleName(),50);
		costs.put(RoadTile.class.getSimpleName(),50);
		costs.put(MountainTile.class.getSimpleName(),50);
		costs.put(TeleportTile.class.getSimpleName(),50);
		costs.put(GoldTile.class.getSimpleName()+"_0",50);
		costs.put(GoldTile.class.getSimpleName()+"_1",150);
		costs.put(GoldTile.class.getSimpleName()+"_9",50);
		List<Service> services2 = new ArrayList<Service>();
		services2.add(new MoveService(costs));
		services2.add(new ViewService());
		services2.add(new NextService());
		AppContext.add(new TestGameTemplate(map, services2));
		
		AppContext.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void testMovementCosts(){
		// Testing the actual default values
		assertEquals(100, DEFAULT_TILE_COSTS[BASE_TILE]);
		assertEquals(200, DEFAULT_TILE_COSTS[CITY_TILE]);
		assertEquals(500, DEFAULT_TILE_COSTS[DEEP_WATER_TILE]);
		assertEquals(150, DEFAULT_TILE_COSTS[SHALLOW_WATER_TILE]);
		assertEquals(100, DEFAULT_TILE_COSTS[GOLD_TILE]);
		assertEquals(300, DEFAULT_TILE_COSTS[FOREST_TILE]);
		assertEquals(25,  DEFAULT_TILE_COSTS[ROAD_TILE]);
		assertEquals(500, DEFAULT_TILE_COSTS[MOUNTAIN_TILE]);
		assertEquals(100, DEFAULT_TILE_COSTS[TELEPORT_TILE]);
		assertEquals(175, DEFAULT_TILE_COSTS[HILL_TILE]);

		// Testing with the default values
		// Viewing first as there seems to be a 200ms performance
		// hit for the first command of the game;
		client.view();
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[BASE_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[CITY_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[DEEP_WATER_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[SHALLOW_WATER_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[HILL_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[FOREST_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[ROAD_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[MOUNTAIN_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[TELEPORT_TILE]);
		moveAndTime(Direction.EAST, DEFAULT_TILE_COSTS[GOLD_TILE]);

		// Testing custom movement costs
		AppContext.progress(AppContext.getPlayer("test"));
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 150);
		moveAndTime(Direction.EAST, 50);
		
	}
	
	private void moveAndTime(Direction d, long time){
		long start = System.currentTimeMillis();
		client.move(d);
		assertEquals(time, System.currentTimeMillis() - start, 30);
	}
}
