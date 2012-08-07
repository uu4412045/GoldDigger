package com.golddigger.model;

import static org.junit.Assert.*;

import java.io.PrintWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.utils.MapMaker;

public class GameTest {
	private static final String contextID = "gameTest";
	@BeforeClass
	public static void before(){
		new AppContext(contextID);
	}
	
	@After
	public void after(){
		AppContext.getContext(contextID).clear();
	}
	
	@AfterClass
	public static void cleanup(){
		AppContext.remove(contextID);
	}
	
	@Test
	public void test() {
		new AppContext(contextID);
		Game game = new Game(0, contextID);
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
		new AppContext(contextID);
		Game game = new Game(0, contextID);
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
		Game game = new Game(0, contextID);
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
		new AppContext(contextID);
		Game game = new Game(0, contextID);
		Service s1 = newStub(Service.BASE_PRIORITY);
		Service s2 = newStub(10);
		Service s3 = newStub(5);
		game.add(s1);
		game.add(s2);
		game.add(s3);

		assertEquals(s2, game.getServices()[0]);
		assertEquals(s3, game.getServices()[1]);
		assertEquals(s1, game.getServices()[2]);
	}
	
	private Service newStub(int priority){
		return new Service(priority){

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
