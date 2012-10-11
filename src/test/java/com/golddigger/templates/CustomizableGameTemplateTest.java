package com.golddigger.templates;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.golddigger.model.Game;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.DeepWaterTile;
import com.golddigger.model.tiles.MountainTile;
import com.golddigger.services.CannonService;
import com.golddigger.services.DayNightService;
import com.golddigger.services.GameService;
import com.golddigger.services.HexMoveService;
import com.golddigger.services.OccludedHexViewService;
import com.golddigger.services.HexViewService;
import com.golddigger.services.MoveService;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.OccludedViewService;
import com.golddigger.services.ViewService;
import com.golddigger.templates.CustomizableGameTemplate;
import com.golddigger.utils.MapMaker;

public class CustomizableGameTemplateTest {
	CustomizableGameTemplate template;
	final String map = "www\nwbw\nwww";
	
	@Before
	public void before(){
		this.template = new CustomizableGameTemplate();
		this.template.setMap(map);
	}
	
	@Test
	public void testSetCosts() {
		List<String> costs = new ArrayList<String>();
		costs.add("b=200");
		costs.add("m=300");
		costs.add("d=1000");
		this.template.setCosts(costs.toArray(new String[]{}));
		Game game = this.template.build();
		MoveService move = null;
		for (GameService service : game.getServices()){
			if (service instanceof MoveService){
				move = (MoveService) service;
			}
		}
		assertNotNull(move);
		assertEquals(200, (int) move.getCost(new BaseTile()));
		assertEquals(300, (int) move.getCost(new MountainTile()));
		assertEquals(1000, (int) move.getCost(new DeepWaterTile()));
	}

	@Test
	public void testDayNight() {
		this.template.setDayNight(3, 400);
		Game game = this.template.build();
		
		DayNightService day = null;
		for (GameService service : game.getServices()){
			if (service instanceof DayNightService){
				day = (DayNightService) service;
			}
		}

		assertNotNull(day);
		assertEquals(3,day.getCycleTime());
		assertEquals(400,day.getScale());
	}

	@Test
	public void testSetLineOfSight() {
		this.template.setLineOfSight(3);
		Game game = this.template.build();
		
		ViewService view = null;
		for (GameService service : game.getServices()){
			if (service instanceof ViewService){
				view = (ViewService) service;
			}
		}
		
		assertEquals(3, view.getLineOfSight());
	}

	@Test
	public void setHexField(){
		this.template.setNumberOfSides(6);
		Game game = this.template.build();
		ViewService view = null;
		MoveService move = null;
		
		for(GameService service: game.getServices()){
			if (service instanceof ViewService){
				view = (ViewService) service;
			} else if (service instanceof MoveService){
				move = (MoveService) service;
			}
		}
		
		assertNotNull(view);
		assertNotNull(move);
		assertTrue(view instanceof HexViewService);
		assertTrue(move instanceof HexMoveService);
	}
	
	@Test
	public void testSquareField() {
		this.template.setNumberOfSides(4);
		Game game = this.template.build();
		ViewService view = null;
		MoveService move = null;
		
		for(GameService service: game.getServices()){
			if (service instanceof ViewService){
				view = (ViewService) service;
			} else if (service instanceof MoveService){
				move = (MoveService) service;
			}
		}
		
		assertNotNull(view);
		assertNotNull(move);
		assertFalse(view instanceof HexViewService);
		assertFalse(move instanceof HexMoveService);
	}

	@Test
	public void testSetMap() {
		Game game = this.template.build();
		assertEquals(this.map, MapMaker.parse(game.getMap()).trim());
	}
	
	@Test
	public void testEnableCannon(){
		Game game;
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		template.setMap("www\nwbw\nwww");
		
		game = template.build();
		assertEquals(0, game.getServices(CannonService.class).size());
		
		template.enableCannons(true);
		game = template.build();
		assertEquals(1, game.getServices(CannonService.class).size());
	}
	
	@Test
	public void setSquareOcclusion(){
		template.enableOcclusion(true);
		template.setNumberOfSides(4);
		Game game = this.template.build();
		ViewService view = null;
		
		for (GameService service : game.getServices()){
			if (service instanceof ViewService){
				view = (ViewService) service;
			}
		}
		assertTrue(view instanceof OccludedViewService);
	}
	
	@Test
	public void setHexOcclusion(){
		template.enableOcclusion(true);
		template.setNumberOfSides(6);
		Game game = this.template.build();
		ViewService view = null;
		
		for (GameService service : game.getServices()){
			if (service instanceof ViewService){
				System.out.println();
				view = (ViewService) service;
			}
		}
		assertTrue(view instanceof OccludedHexViewService);
	}
}
