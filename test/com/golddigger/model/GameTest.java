package com.golddigger.model;

import static org.junit.Assert.*;

import org.junit.Test;

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

}
