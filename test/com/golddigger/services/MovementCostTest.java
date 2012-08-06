package com.golddigger.services;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.TestServer;
import com.golddigger.client.TestingClient;
import com.golddigger.core.AppContext;
import com.golddigger.model.Player;
import com.golddigger.model.tiles.*;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.generators.BaseServiceGenerator;


public class MovementCostTest {
	TestServer server;
	TestingClient client;
	private static final String MAP = "wwwwwwwwwwwwww\nwbbcdshfrmt.19w\nwwwwwwwwwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new TestServer();
		server.getContext().add(new TestGameTemplate(MAP));
		
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
		BaseServiceGenerator gen = new BaseServiceGenerator();
		gen.setCosts(costs);
		server.getContext().add(new TestGameTemplate(MAP, gen));
		
		server.getContext().add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void testMovementCosts(){

		// Testing with the default values
		// Viewing first as there seems to be a 200ms performance
		// hit for the first command of the game;
		client.view();
		moveAndTime(Direction.EAST, BaseTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, CityTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, DeepWaterTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, ShallowWaterTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, HillTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, ForestTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, RoadTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, MountainTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, TeleportTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, GoldTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, GoldTile.DEFAULT_MOVEMENT_COST);
		moveAndTime(Direction.EAST, GoldTile.DEFAULT_MOVEMENT_COST);

		// Testing custom movement costs
		server.getContext().progress(server.getContext().getPlayer("test"));
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
		assertEquals(time, System.currentTimeMillis() - start, 20);
	}
}
