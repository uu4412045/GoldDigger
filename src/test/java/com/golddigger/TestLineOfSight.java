package com.golddigger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.services.HexViewService;
import com.golddigger.services.ViewService;
import com.golddigger.utils.MapMaker;
import com.golddigger.utils.TestWriter;

@RunWith(MockitoJUnitRunner.class)
public class TestLineOfSight {
	private final String MAP_STRING = "wwwww\nw...w\nw.b.w\nw...w\nwwwww\n";
	private final String name = "test";
	ViewService view;
	@Mock Game game;
	@Mock Player player;
	Unit unit;
	TestWriter writer;
        
    @Before
    public void before() {
    	unit = new Unit(player, new Coordinate(2,2));
    	writer = new TestWriter();
    	Map map = MapMaker.parse(MAP_STRING);
    	when(game.getMap()).thenReturn(map);
    	when(game.getPlayer(name)).thenReturn(player);
    	when(game.getUnit(player)).thenReturn(unit);
    }
    
    // Test the sight view for the sides
    @Test
    public void squareViewLOS1() throws Exception {
    	view = new ViewService(1);
    	view.setGame(game);
    	
        moveAndAssert(Direction.NORTH, "www\n...\n.b.");
        moveAndAssert(Direction.EAST,  "www\n..w\nb.w");
        moveAndAssert(Direction.SOUTH, "..w\nb.w\n..w");
        moveAndAssert(Direction.SOUTH, "b.w\n..w\nwww");
        moveAndAssert(Direction.WEST,  ".b.\n...\nwww");
        moveAndAssert(Direction.WEST,  "w.b\nw..\nwww");
        moveAndAssert(Direction.NORTH, "w..\nw.b\nw..");
        moveAndAssert(Direction.NORTH, "www\nw..\nw.b");
    }
    
    @Test
    public void squareViewLOS2() throws Exception {
    	view = new ViewService(2);
    	view.setGame(game);
        moveAndAssert(Direction.NORTH, "-----\nwwwww\nw...w\nw.b.w\nw...w");
        moveAndAssert(Direction.EAST,  "-----\nwwww-\n...w-\n.b.w-\n...w-");
        moveAndAssert(Direction.SOUTH, "wwww-\n...w-\n.b.w-\n...w-\nwwww-");
        moveAndAssert(Direction.SOUTH, "...w-\n.b.w-\n...w-\nwwww-\n-----");
        moveAndAssert(Direction.WEST,  "w...w\nw.b.w\nw...w\nwwwww\n-----");
        moveAndAssert(Direction.WEST,  "-w...\n-w.b.\n-w...\n-wwww\n-----");
        moveAndAssert(Direction.NORTH, "-wwww\n-w...\n-w.b.\n-w...\n-wwww");
        moveAndAssert(Direction.NORTH, "-----\n-wwww\n-w...\n-w.b.\n-w...");
    }
    
    @Test
    public void hexViewLOS1(){
    	view = new HexViewService(1);
    	view.setGame(game);
        moveAndAssert(Direction.NORTH,      "www\n...\n?b?");
        moveAndAssert(Direction.SOUTH_EAST, "?w?\n..w\nb.w");
        moveAndAssert(Direction.SOUTH,      "?.?\nb.w\n..w");
    	moveAndAssert(Direction.SOUTH_WEST, ".b.\n...\n?w?");
    	moveAndAssert(Direction.NORTH_WEST, "?.?\nw.b\nw..");
    	moveAndAssert(Direction.NORTH,      "?w?\nw..\nw.b");
    } 
    
    @Test
    public void hexViewLOS2(){
    	view = new HexViewService(2);
    	view.setGame(game);
        moveAndAssert(Direction.NORTH,      "-----\nwwwww\nw...w\nw.b.w\n??.??");
        moveAndAssert(Direction.SOUTH_EAST, "-----\nwwww-\n...w-\n.b.w-\n?..w-");
        moveAndAssert(Direction.SOUTH,      "??w?-\n...w-\n.b.w-\n...w-\n?www-");
        moveAndAssert(Direction.SOUTH_WEST, "?...?\nw.b.w\nw...w\nwwwww\n-----");
        moveAndAssert(Direction.NORTH_WEST, "-?w??\n-w...\n-w.b.\n-w...\n-www?");
        moveAndAssert(Direction.NORTH,      "-----\n-wwww\n-w...\n-w.b.\n-w..?");
    }

	private void moveAndAssert(Direction direction, String expected) {
		move(direction);
		String url = "http://localhost/golddigger/digger/"+name+"/view";
		assertTrue(view.runnable(url));
		assertTrue(view.execute(url, writer.getPrintWriter()));
		assertEquals(expected.trim(), writer.getHistory().trim());
		writer.clear();
	}
	
	private void move(Direction direction){
		Coordinate pos = direction.getOffset(unit.getPosition());
		unit.setPosition(pos);
	}
}
