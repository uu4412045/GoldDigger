package com.golddigger;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.client.TestingClient;
import com.golddigger.core.AppContext;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;

public class MultiplayerTest {
	private static final String MAP_1 = "www\nwbw\nwww";
	private static final String MAP_2 = "wwwww\nwb1bw\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";
	private TestServer server;
	private TestingClient player1;
	private TestingClient player2;
	

	@Before()
	public void setup(){
		server = new TestServer();
		server.getContext().add(new TestGameTemplate(MAP_1));
		server.getContext().add(new TestGameTemplate(MAP_2));
		server.getContext().add(new TestGameTemplate(MAP_1));
		server.getContext().add(new Player("test1", "secret"));
		server.getContext().add(new Player("test2", "secret"));
		player1 = new TestingClient("test1", BASE_URL);
		player2 = new TestingClient("test2", BASE_URL);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void testMultiplayer() {
		Player p1 = server.getContext().getPlayer("test1");
		Player p2 = server.getContext().getPlayer("test2");
		assertNotSame(server.getContext().getGame(p1), server.getContext().getGame(p2));
		
		//Test Joining
		assertEquals("OK", player1.next().trim());
		assertEquals("OK", player2.next().trim());
		assertEquals(server.getContext().getGame(p1), server.getContext().getGame(p2));
		
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
		assertNotSame(server.getContext().getGame(p1), server.getContext().getGame(p2));
	}

}
