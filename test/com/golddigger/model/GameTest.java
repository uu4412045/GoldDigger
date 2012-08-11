package com.golddigger.model;

import static org.junit.Assert.*;

import java.io.PrintWriter;

import org.junit.Test;

import com.golddigger.core.GameService;
import com.golddigger.core.Service;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.utils.MapMaker;

public class GameTest {
	
	@Test
	public void test() {
		Game game = new Game(0);
		Map map = new BlankMap(3,3);
		game.setMap(map);
		Player player = new Player("test", "secret");
		assertTrue(game.hasUnownedBase());
		game.add(player);
		assertTrue(game.hasPlayer(player));
		Unit unit = game.getUnit(player);
		assertNotNull("Unit should not be null", unit);
		assertEquals(new Point2D(1,1),unit.getPosition());
	}
	
	@Test
	public void testBaseFunctions(){
		Game game = new Game(0);
		Map map = new BlankMap(3,3);
		game.setMap(map);
		BaseTile[] bases = game.getBases();
		assertEquals(1, bases.length);
		assertTrue(bases[0].getOwner() == null);
		assertTrue(game.hasUnownedBase());
	}
	
	@Test
	public void testMultiplayer(){
		String string_map = "wwwww\nwb.bw\nwwwww";
		Game game = new Game(0);
		Map map = MapMaker.parse(string_map);
		game.setMap(map);
		Player player1 = new Player("player1","secret");
		Player player2 = new Player("player2","secret");
		
		assertEquals(2, game.getBases().length);
		assertTrue(game.hasUnownedBase());
		
		game.add(player1);
		assertTrue(game.hasUnownedBase());
		assertTrue(game.hasPlayer(player1));
		
		game.add(player2);
		assertFalse(game.hasUnownedBase());
		assertTrue(game.hasPlayer(player1));
		assertTrue(game.hasPlayer(player2));
	}
	
	@Test
	public void testServiceOrder(){
		Game game = new Game(0);
		GameService s1 = newStub(Service.BASE_PRIORITY);
		GameService s2 = newStub(10);
		GameService s3 = newStub(5);
		game.add(s1);
		game.add(s2);
		game.add(s3);

		assertEquals(s2, game.getServices()[0]);
		assertEquals(s3, game.getServices()[1]);
		assertEquals(s1, game.getServices()[2]);
	}
	
	private GameService newStub(int priority){
		return new GameService(priority){

			@Override
			public boolean runnable(String url) {
				return false;
			}

			@Override
			public boolean execute(String url, PrintWriter out) {
				return false;
			}
			
		};
	}

}
