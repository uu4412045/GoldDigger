package com.golddigger.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.utils.MapMaker;
import com.golddigger.utils.TestWriter;

@RunWith(MockitoJUnitRunner.class)
public class OccludedViewServiceTest {
	private static final String VIEW_URL = "http://localhost/golddigger/digger/test/view";
	TestWriter writer;
	@Mock Player player;
	@Spy Game game = new Game(0);
	Unit unit;
	OccludedViewService occluded_view;

	@Before()
	public void setup(){
		writer = new TestWriter();
		when(player.getName()).thenReturn("test");
		occluded_view = new OccludedViewService(1);
		game.add(occluded_view);
	}
	
	@Test
	public void occludedLOS2() {
		String map = 
				"wwwwwwww\n"+
				"wb.....w\n"+
				"w....w.w\n"+
				"w......w\n"+
				"w.w....w\n"+
				"w......w\n"+
				"wwwwwwww\n";
		
		occluded_view.setLineOfSight(2);
		game.setMap(MapMaker.parse(map));
		game.add(player);
		unit = game.getUnit(player);
		
		unit.setPosition(5, 1);
		moveAndAssert(Direction.EAST, "w???.\nw.w..\nw....\n?www?\n-----\n");
		moveAndAssert(Direction.WEST, "-?.??\n-w.w?\n-w...\n-www?\n-----\n");
		moveAndAssert(Direction.NORTH, "-?...\n-w..?\n-w.w?\n-w..?\n-?www\n");
		
		unit.setPosition(1,3);
		moveAndAssert(Direction.EAST, "-----\n?www?\n.....\n...w?\n...??\n");
		moveAndAssert(Direction.EAST, "-----\n?www?\n....w\n..w.w\n.???w\n");
		moveAndAssert(Direction.EAST, "-----\n?www-\n...w-\n?w.w-\n??.?-\n");
		moveAndAssert(Direction.SOUTH, "www?-\n?..w-\n?w.w-\n?..w-\n...?-\n");
		moveAndAssert(Direction.SOUTH, "??.?-\n?w.w-\n...w-\n...w-\n...?-\n");
	}

	@Test
	public void occludedLOS3() {
		String map =
				"wwwwwwwww\n"+
				"wb......w\n"+
				"w.......w\n"+
				"w.......w\n"+
				"w....w..w\n"+
				"w.......w\n"+
				"w.......w\n"+
				"w.......w\n"+
				"wwwwwwwww\n";
				
		occluded_view.setLineOfSight(3);
		game.setMap(MapMaker.parse(map));
		game.add(player);
		unit = game.getUnit(player);
		
		unit.setPosition(6,4);
		moveAndAssert(Direction.EAST, "...?..w\n...w..w\n......w\n......w\n......w\n?wwwww?\n-------\n");
		move(Direction.WEST);
		moveAndAssert(Direction.WEST, "w....??\nw....w?\nw......\nw......\nw......\n?wwwww?\n-------\n");
		move(Direction.NORTH);
		moveAndAssert(Direction.NORTH, "wb.....\nw......\nw......\nw....w?\nw......\nw......\nw......\n");
		move(Direction.NORTH);
		moveAndAssert(Direction.NORTH, "-------\n?wwwww?\nwb.....\nw......\nw......\nw....w?\nw....??\n");
		move(Direction.EAST);
		moveAndAssert(Direction.EAST, "-------\n?wwwww?\n......w\n......w\n......w\n...w..w\n...?..w\n");
		move(Direction.EAST);
		moveAndAssert(Direction.EAST, "-------\n?www?--\n....w--\n....w--\n....w--\n?w..?--\n??..?--\n");
		move(Direction.SOUTH);
		moveAndAssert(Direction.SOUTH, "....?--\n....?--\n....w--\n?w..w--\n....w--\n....?--\n....?--\n");
		move(Direction.SOUTH);
		moveAndAssert(Direction.SOUTH, "??..?--\n?w..?--\n....w--\n....w--\n....w--\n?www?--\n-------\n");
	}

	public void moveAndAssert(Direction d, String expected){
		move(d);
		assertTrue(occluded_view.runnable(VIEW_URL));
		assertTrue(occluded_view.execute(VIEW_URL, writer.getPrintWriter()));
		assertEquals(expected.trim(), writer.getHistory().trim());
		writer.clear();
	}
	
	public void move(Direction d){
		unit.setPosition(d.getOffset(unit.getPosition()));
	}
}
