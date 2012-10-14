package com.golddigger.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Coordinate;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.utils.MapMaker;

@RunWith(MockitoJUnitRunner.class)
public class DisadvanageousTeleportationTest {
	private static final String MAP = "wwwwwwww\n" +
			"wb.....w\n" + 
			"w...w..w\n" + 
			"wwwwwwww\n";

	@Mock PrintWriter writer;
	MoveService service;
	Game game;
	Player player = new Player("test", "secret");

	@Before()
	public void setup(){
		game = new Game(-1);
		service = new SquareMoveService();
		service.setGame(game);
		game.setMap(MapMaker.parse(MAP));
		game.add(player);
	}


	@Test
	public void teleport(){
		String url = "http://server/golddigger/digger/test/move/east";
		Coordinate src = new Coordinate(1,2);
		Coordinate dest = new Coordinate(1,6);
		game.getMap().get(src).setTeleportDestination(dest);

		boolean runnable = service.runnable(url);
		boolean consumed = service.execute(url, writer);

		assertTrue(runnable && consumed);
		verify(writer).println("OK: Teleported to (0,4)");
		assertEquals(dest, game.getUnit(player).getPosition());
	}
	
	@Test
	public void noTeleportBackwards(){
		String url = "http://server/golddigger/digger/test/move/east";
		Coordinate src = new Coordinate(1,2);
		Coordinate dest = new Coordinate(1,6);
		game.getMap().get(src).setTeleportDestination(dest);
		game.getUnit(player).setPosition(1, 5); // one step west of dest
		
		boolean runnable = service.runnable(url);
		boolean consumed = service.execute(url, writer);
		
		assertTrue(runnable && consumed);
		verify(writer).println("OK");
		assertEquals(dest, game.getUnit(player).getPosition());
	}

	@Test
	public void onlyTeleportOnce(){
		String url = "http://server/golddigger/digger/test/move/east";
		Coordinate src = new Coordinate(1,2);
		Coordinate dest = new Coordinate(1,6);
		Coordinate dest2 = new Coordinate(1,3);
		game.getMap().get(src).setTeleportDestination(dest);
		game.getMap().get(dest).setTeleportDestination(dest2);

		boolean runnable = service.runnable(url);
		boolean consumed = service.execute(url, writer);

		assertTrue(runnable && consumed);
		verify(writer).println("OK: Teleported to (0,4)");
		assertEquals(dest, game.getUnit(player).getPosition());
	}

	@Test
	public void teleportOntoUnit(){
		String url = "http://server/golddigger/digger/test/move/east";
		Coordinate src = new Coordinate(1,2);
		Coordinate dest = new Coordinate(1,6);
		Coordinate dest2 = new Coordinate(1,3);
		game.getMap().set(1,6, new BaseTile());
		game.add(new Player("test2", "secret2"));
		game.getMap().get(src).setTeleportDestination(dest);
		game.getMap().get(dest).setTeleportDestination(dest2);

		boolean runnable = service.runnable(url);
		boolean consumed = service.execute(url, writer);

		assertEquals(dest, game.getUnit(game.getPlayer("test2")).getPosition());
		assertTrue(runnable && consumed);
		verify(writer).println("FAILED");
		assertEquals(src, game.getUnit(player).getPosition());
	}	
}
