package com.golddigger.core.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.model.Player;
import com.golddigger.server.GameServer;
import com.golddigger.templates.GameTemplate;
import com.golddigger.templates.TestGameTemplate;

public class GameServerTest {
	private GameServer server;
	
	@Before
	public void before(){
		server = new GameServer();
	}
	
	@After
	public void after(){
		server.clear();
	}

	@Test
	public void testAddGameTemplate() {
		GameTemplate t1 = new TestGameTemplate("www/nwbw/nwww/n");
		GameTemplate t2 = new TestGameTemplate("wwww/nwb.w/nwwww/n");
		
		server.add(t1);
		server.add(t2);
		
		// Making sure the template ID's are being set correctly
		assertEquals("Template IDs should start at 0",0,t1.getID());
		assertEquals("Template IDs should increment by 1",1,t2.getID());
	}

	@Test
	public void testAddPlayer() {
		GameTemplate t1 = new TestGameTemplate("www/nwbw/nwww/n");
		GameTemplate t2 = new TestGameTemplate("wwwww/nwb.bw/nwwwww/n");
		Player p1 = new Player("test1", "secret1");
		Player p2 = new Player("test2","secret2");
		
		server.add(t1);
		server.add(t2);
		server.add(p1);

		assertEquals(p1, server.getPlayer("secret1"));
		assertNull("Should return null if there is no player by that name",server.getPlayer("incorrect name"));
		
		assertNotNull("Player not added to a game",server.getGame(p1));
		assertEquals("Game not created from the first template", 0, server.getGame(p1).getTemplateID());
		server.progress(p1);
		
		server.add(p2);
		assertEquals(p2, server.getPlayer("secret2"));
		assertNotNull(server.getGame(p2));
		assertNotSame("Should not have been added to the 2nd game",server.getGame(p1), server.getGame(p2));
		assertEquals("Game not created from the first template", 0, server.getGame(p2).getTemplateID());
		
		// Testing adding the same player twice
		assertFalse(server.add(p2));
	}

	@Test
	public void testProgress() {
		GameTemplate t1 = new TestGameTemplate("www/nwbw/nwww/n");
		GameTemplate t2 = new TestGameTemplate("wwwww/nwb.bw/nwwwww/n");
		Player p1 = new Player("test1", "secret1");
		Player p2 = new Player("test2","secret2");
		
		server.add(t1);
		server.add(t2);
		server.add(p1);
		server.add(p2);
		
		server.progress(p1);
		assertNotNull(server.getGame(p1));
		assertEquals("Game should have been build from the 2nd template",1,server.getGame(p1).getTemplateID());
		assertEquals("Player2 should still be on the first game",0,server.getGame(p2).getTemplateID());
		
		server.progress(p2);
		assertSame("Players should be on the multiplayer map",server.getGame(p1), server.getGame(p2));
		
		server.progress(p1);
		assertEquals("Player1 should be at the start again", 0, server.getGame(p1).getTemplateID());
	}

	@Test
	public void testClear() {
		GameTemplate t1 = new TestGameTemplate("www/nwbw/nwww/n");
		Player p1 = new Player("test", "secret");
		
		server.add(t1);
		server.add(p1);
		
		server.clear();
		assertNull("Games should have been removed",server.getPlayer("secret"));
		assertNull("Games should have been removed",server.getGame(p1));
	}

}
