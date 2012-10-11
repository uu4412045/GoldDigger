package com.golddigger.services;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
public class SquareMoveServiceTest {
	private static final String MAP =
			"wwwww\n"+
			"w...w\n"+
			"w.b.w\n"+
			"w...w\n"+
			"wwwww";

	final String name = "test";
	SquareMoveService service;
	@Mock Game game;
	@Mock Player player;
	@Spy Unit unit = new Unit(player, new Coordinate(2,2));

	private TestWriter writer;

	@Before
	public void before() {
		HashMap<String, Integer> costs = new HashMap<String, Integer>();
		costs.put(new GoldTile().toString(), 0);
		service = new SquareMoveService(costs);
		writer = new TestWriter();
		service.setGame(game);
		Map map = MapMaker.parse(MAP);
		when(game.getMap()).thenReturn(map);
		when(game.getPlayer(name)).thenReturn(player);
		when(game.getUnit(player)).thenReturn(unit);
	}

	@Test
	public void testInvalidMoves(){
		//Make sure invalid directions fail
		moveAndAssert(Direction.NORTH_EAST, false);
		moveAndAssert(Direction.NORTH_WEST, false);
		moveAndAssert(Direction.SOUTH_EAST, false);
		moveAndAssert(Direction.SOUTH_WEST, false);
	}
	
	@Test
	public void testValidMoves(){
		// Testing valid moves
		moveAndAssert(Direction.NORTH, true);
		moveAndAssert(Direction.EAST, true);
		moveAndAssert(Direction.SOUTH, true);
		moveAndAssert(Direction.WEST, true);
	}
	
	@Test
	public void testMapBounds() throws Exception {
		//Should't move outside north bounds
		moveAndAssert(Direction.NORTH, true);
		moveAndAssert(Direction.NORTH, false);
		
		//Shouldn't move outside south bounds
		moveAndAssert(Direction.SOUTH, true);
		moveAndAssert(Direction.SOUTH, true);
		moveAndAssert(Direction.SOUTH, false);
		
		//Shouldn't move outside east bounds
		moveAndAssert(Direction.NORTH,true);
		moveAndAssert(Direction.EAST, true);
		moveAndAssert(Direction.EAST, false);
		
		//Shouldn't move outside west bounds
		moveAndAssert(Direction.WEST, true);
		moveAndAssert(Direction.WEST, true);
		moveAndAssert(Direction.WEST, false);
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
