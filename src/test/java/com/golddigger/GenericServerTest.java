package com.golddigger;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Unit;
import com.golddigger.server.GameServer;
import com.golddigger.templates.GameTemplate;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.MapMaker;

public class GenericServerTest {
	GenericServer server;

	@Test
	public void testGenericServer() {
		 server = new GenericServer();
		assertNull(server.getDelayed());
		assertNotNull(server.getMain());
		server.stop();
	}
	
	@After
	public void after(){
		server.stop();
	}

	@Test
	public void testGenericServerLong() throws InterruptedException {
		server = new GenericServer(1000, true);
		assertNotNull(server.getDelayed());
		assertNotNull(server.getMain());
		
		GameServer main = server.getMain();
		GameServer delayed = server.getDelayed();
		
		String name = "test";
		server.addTemplate(new TestGameTemplate("wwww\nwb.w\nwwww"));
		server.addPlayer(name, "secret");
		TestingClient client = new TestingClient(name, "http://localhost:8066");

		Player mainPlayer = main.getPlayer(name);
		Player delayedPlayer = delayed.getPlayer(name);

		Unit mainUnit = main.getGame(mainPlayer).getUnit(mainPlayer);
		Unit delayedUnit = delayed.getGame(delayedPlayer).getUnit(delayedPlayer);
		
		Point2D mainPos = mainUnit.getPosition();
		Point2D delayedPos = delayedUnit.getPosition();
		
		client.move(Direction.EAST);
		
		Point2D newPos = mainUnit.getPosition();
		assertEquals(Direction.EAST.getOffset(mainPos), newPos);
		assertEquals(delayedPos, delayedUnit.getPosition());
		Thread.sleep(2000);
		assertEquals(newPos, delayedUnit.getPosition());
		
		server.stop();
	}

	@Test
	public void testAddPlayer() {
		server = new GenericServer(1000, true);

		GameServer main = server.getMain();
		GameServer delayed = server.getDelayed();

		String name = "test";
		server.addPlayer(name, "secret");
		assertNotNull(main.getPlayer(name));
		assertNotNull(delayed.getPlayer(name));

		server.stop();
	}
	
	@Test
	public void testAdminServices(){
		server = new GenericServer();
		server.addPlayer("test1","Secrt");
		TestingClient client = new TestingClient(null, null);
		String before = client.doGET("http://localhost:8066/golddigger/admin/ccret/listdiggers").trim();
		assertEquals("OK",client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test8/secret").trim());
		String after = client.doGET("http://localhost:8066/golddigger/admin/ccret/listdiggers").trim();
		assertEquals(before+"\ntest8 secret", after);
		server.stop();
	}

	@Test
	public void testAddTemplate() {
		server = new GenericServer(1000, true );

		GameServer main = server.getMain();
		GameServer delayed = server.getDelayed();

		String map = "www\nwbw\nwww\n";
		String name = "test";
		GameTemplate template = new TestGameTemplate(map);
		server.addTemplate(template);
		server.addPlayer(name, "secret");

		Game mainGame = main.getGame(main.getPlayer(name));
		Game delayedGame = delayed.getGame(delayed.getPlayer(name));

		assertNotNull(mainGame);
		assertNotNull(delayedGame);

		assertEquals(map, MapMaker.parse(mainGame.getMap()));
		assertEquals(map, MapMaker.parse(delayedGame.getMap()));

		server.stop();
	}

}
