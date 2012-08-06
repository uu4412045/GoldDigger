package com.golddigger.model;

import static org.junit.Assert.*;

import java.io.PrintWriter;

import org.junit.Test;

import com.golddigger.core.Service;

public class GameTest {

	@Test
	public void test() {
		Game game = new Game(0);
		game.setMap(new BlankMap(3,3));
		Player player = new Player("test", "secret");
		game.add(player);
		assertTrue(game.hasPlayer(player));
		Unit unit = game.getUnit(player);
		assertNotNull("Unit should not be null", unit);
		assertEquals(new Point2D(1,1),unit.getPosition());
		
	}
	
	@Test
	public void testServiceOrder(){
		Game game = new Game(0);
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
