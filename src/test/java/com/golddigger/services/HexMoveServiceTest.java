package com.golddigger.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.golddigger.model.Coordinate;
import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.utils.MapMaker;
import com.golddigger.utils.TestWriter;

@RunWith(MockitoJUnitRunner.class)
public class HexMoveServiceTest {
	private static final String MAP = 
					"wwwwww\n"+
					"wb...w\n"+
					"w....w\n"+
					"w....w\n"+
					"w....w\n"+
					"wwwwww";

	final String name = "test";
	HexMoveService service;
	@Mock Game game;
	@Mock Player player;
	@Spy Unit unit = new Unit(player, new Coordinate(2,3));

	private TestWriter writer;

	@Before
	public void before() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put(new GoldTile().toString(), 0);
		service = new HexMoveService(costs);
		writer = new TestWriter();
		service.setGame(game);
		Map map = MapMaker.parse(MAP);
		when(game.getMap()).thenReturn(map);
		when(game.getPlayer(name)).thenReturn(player);
		when(game.getUnit(player)).thenReturn(unit);
	}

	@Test
	public void canMoveInAllDirections() throws Exception {
		moveAndAssert(Direction.NORTH, true);
		moveAndAssert(Direction.SOUTH_EAST, true);
		moveAndAssert(Direction.SOUTH, true);
		moveAndAssert(Direction.SOUTH, true);
		moveAndAssert(Direction.SOUTH_WEST, true);
		moveAndAssert(Direction.NORTH_WEST, true);
		moveAndAssert(Direction.NORTH_EAST, true);
	}

	@Test
	public void wontMoveOutsideNorthBounds() throws Exception {
		moveAndAssert(Direction.NORTH, true);
		moveAndAssert(Direction.NORTH, false);
	}

	@Test
	public void wontMoveOutsideSouthBounds() throws Exception {
		moveAndAssert(Direction.SOUTH, true);
		moveAndAssert(Direction.SOUTH, true);
		moveAndAssert(Direction.SOUTH, false);        
	}

	@Test
	public void wontMoveOutsideNorthEastBounds() throws Exception {
		moveAndAssert(Direction.NORTH_EAST, true);
		moveAndAssert(Direction.NORTH_EAST, false);
	}

	@Test
	public void wontMoveOutsideSouthWestBounds() throws Exception {
		moveAndAssert(Direction.SOUTH_WEST, true);
		moveAndAssert(Direction.SOUTH_WEST, true);
		moveAndAssert(Direction.SOUTH_WEST, false);

	}

	@Test
	public void wontMoveOutsideSouthEastBounds() throws Exception {
		moveAndAssert(Direction.SOUTH_EAST, true);       
		moveAndAssert(Direction.SOUTH_EAST, false);
	}
	@Test
	public void wontMoveOutsideNorthWestBounds() throws Exception {
		moveAndAssert(Direction.NORTH_WEST, true);        
		moveAndAssert(Direction.NORTH_WEST, true);        
		moveAndAssert(Direction.NORTH_WEST, false);
	}

	private void moveAndAssert(Direction direction, boolean successful) {
		String url = "http://localhost/golddigger/digger/"+name+"/move/"+direction;
		Coordinate target = direction.getOffset(unit.getPosition()); 
		assertTrue(service.runnable(url));
		assertTrue(service.execute(url, writer.getPrintWriter()));
		
		if (successful){
			assertEquals("OK", writer.getHistory().trim());
			verify(unit).setPosition(target);
		} else {
			assertEquals("FAILED", writer.getHistory().trim());
			verify(unit, never()).setPosition(any(Coordinate.class));
		}
		
		reset(unit); // reseting the call counter in the mock. Try to avoid this.
		writer.clear();
	}

}
