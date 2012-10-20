package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.server.GameServer;
import com.golddigger.templates.CustomizableGameTemplate;

public class MultiplayerServiceTest {
	private GenericServer server;
	private TestingClient p1, p2;
	
	@Before
	public void before(){
		server = new GenericServer();
		p1 = new TestingClient("secret1", "http://localhost:8066");
		p2 = new TestingClient("secret2", "http://localhost:8066");
		
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		template.setMultiplayer(1000, 2000, 1000);
		template.setMap("wwwwww\nwb.4.bw\nw..b..w\nwwwww");
		server.addTemplate(template);
		server.addPlayer("player1", "secret1");
		server.addPlayer("player2", "secret2");
	}
	
	@After
	public void after(){
		server.stop();
	}
	
	
	@Test
	public void testTimers() throws InterruptedException {
		GameServer engine = server.getMain();
		Game game = engine.getGame(engine.getPlayer("secret1"));
		
		//Should not be able to execute commands yet
		assertEquals("FAILED: Joining Only", p1.move(Direction.EAST).trim());
		assertTrue(p1.view().contains("FAILED"));
		assertEquals("FAILED: Joining Only", p2.move(Direction.WEST).trim());
		
		// Wait for the game to start
		Thread.sleep(1000);
		assertEquals("OK", p1.move(Direction.EAST).trim());
		assertEquals("The third base should have been removed",2, game.getBases().length);
		
		assertEquals("OK", p1.move(Direction.EAST).trim());
		assertEquals("OK", p2.move(Direction.WEST).trim());
		assertEquals("3", p1.grab().trim());

		assertEquals("OK", p1.move(Direction.WEST).trim());
		assertEquals("OK", p1.move(Direction.WEST).trim());
		assertEquals("3", p1.drop().trim());

		assertEquals("OK", p2.move(Direction.WEST).trim());
		assertEquals("1", p2.grab().trim());
		
		assertEquals("OK", p1.next().trim());
		assertEquals("FAILED", p2.next().trim());
		
		Thread.sleep(3000);
		assertEquals("FAILED: Game Over", p2.drop().trim());
	}
	
	@Test
	public void testSpying() throws InterruptedException{
		Thread.sleep(1000);

		assertEquals("OK", p1.move(Direction.EAST).trim());
		assertEquals("OK", p1.move(Direction.EAST).trim());
		assertEquals("3", p1.grab().trim());

		assertEquals("OK", p1.move(Direction.WEST).trim());
		assertEquals("OK", p1.move(Direction.WEST).trim());
		assertEquals("3", p1.drop().trim());
		assertEquals("OK", p1.move(Direction.SOUTH).trim());
	
		assertEquals("OK", p2.move(Direction.WEST).trim());
		assertEquals("OK", p2.move(Direction.WEST).trim());
		assertEquals("OK", p2.move(Direction.WEST).trim());
		assertEquals("OK", p2.move(Direction.WEST).trim());
		assertEquals("3 player1", p2.score().trim());
		
		//Testing that the last timer starts when the last gold
		// is picked up.
		assertEquals("OK", p2.move(Direction.EAST).trim());
		assertEquals("OK", p2.move(Direction.EAST).trim());
		assertEquals("1", p2.grab().trim());
		assertEquals("OK", p2.move(Direction.EAST).trim());
		assertTrue(p2.view().startsWith("state: ending"));
	}

}
