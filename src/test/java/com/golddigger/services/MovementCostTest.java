package com.golddigger.services;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.ServletServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.model.tiles.*;
import com.golddigger.services.generators.BaseServiceGenerator;
import com.golddigger.services.generators.ServiceGenerator;
import com.golddigger.templates.CustomizableGameTemplate;
import com.golddigger.templates.TestGameTemplate;


public class MovementCostTest {
	ServletServer server;
	TestingClient client;
	private static final String MAP = "wwwwwwwwwwwwww\nwbbcdshfrmt.19w\nwwwwwwwwwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new ServletServer();
		
		String[] costs = {"b=50","c=50","d=50", "s=50", "h=50", "f=50","r=100","m=50","t=50",".=50", "1=150", "9=50" };
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		CustomizableGameTemplate template1 = new CustomizableGameTemplate();
		template.setMap(MAP);
		template1.setMap(MAP);
		template1.setCosts(costs);
		server.add(template);
		server.add(template1);
		
		server.add(new Player("test", "secret"));
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
//		// hit for the first command of the game;
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
		server.progress(server.getPlayer("test"));
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 100);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 50);
		moveAndTime(Direction.EAST, 150);
		moveAndTime(Direction.EAST, 50);
		
	}
	
	private void moveAndTime(Direction d, long time){
		long start = System.currentTimeMillis();
		client.move(d);
		assertEquals(time, System.currentTimeMillis() - start, 40);
	}
}
