package com.golddigger;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.services.NextService;
import com.golddigger.templates.TestGameTemplate;

public class MultiplayerTest {
	private static final String MAP_1 = "www\nwbw\nwww";
	private static final String MAP_2 = "wwwwww\nwb1b.w\nwwwwww";
	private static final String BASE_URL = "http://localhost:8066";
	private ServletServer server;
	private TestingClient player1;
	private TestingClient player2;
	

	@Before()
	public void setup(){
		server = new ServletServer();
		server.add(new TestGameTemplate(MAP_1));
		server.add(new TestGameTemplate(MAP_2));
		server.add(new TestGameTemplate(MAP_1));
		server.add(new Player("test1", "secret1"));
		server.add(new Player("test2", "secret2"));
		server.add(new NextService());
		player1 = new TestingClient("secret1", BASE_URL);
		player2 = new TestingClient("secret2", BASE_URL);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void testMultiplayer() {
		Player p1 = server.getPlayer("secret1");
		Player p2 = server.getPlayer("secret2");
		
		assertNotSame(server.getGame(p1), server.getGame(p2));
		
		//Test Joining
		assertEquals("OK", player1.next().trim());
		assertEquals("OK", player2.next().trim());
		assertEquals("Players should be in the multiplayer game", server.getGame(p1), server.getGame(p2));
		
		//Test move collision
		assertEquals("FAILED", player1.next().trim());
		player2.move(Direction.WEST);
		assertEquals("FAILED", player2.move(Direction.WEST).trim());
		player2.grab();
		
		//Test next game
		assertEquals("OK", player1.next().trim());
		assertEquals("FAILED", player2.next().trim());
		player2.move(Direction.EAST);
		player2.drop();
		assertEquals("OK", player2.next().trim());
		assertNotSame(server.getGame(p1), server.getGame(p2));
	}
	
	@Test
	public void testMultiplayer2() {
		Player p1 = server.getPlayer("secret1");
		Player p2 = server.getPlayer("secret2");
		
		assertEquals("OK", player1.next().trim());
		assertEquals("OK", player2.next().trim());
		
		player2.move(Direction.WEST);
		assertEquals("FAILED", player2.move(Direction.WEST).trim());
		player2.grab();
		
		player2.move(Direction.EAST);
		player2.drop();
		player2.move(Direction.EAST);
		
		player1.move(Direction.EAST);
		player1.move(Direction.EAST);
		assertEquals("1 "+p2.getName(), player1.score().trim());
	}

}
