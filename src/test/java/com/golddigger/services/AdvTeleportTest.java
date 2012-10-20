package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.ServletServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Map;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.TeleportTile;
import com.golddigger.templates.CustomizableGameTemplate;

public class AdvTeleportTest {
	ServletServer server;
	TestingClient client, client2;
	private static final String BASEMAP = 	"wwwwwwww\n" +
			  								"wbc....w\n" + 
			  								"w...w.bw\n" + 
			  								"wwwwwwww\n";
	private static final String MAP = "wwwwwwww\n" +
									  "wbc....w\n" + 
									  "w...h.bw\n" + 
									  "wwwwwwww\n";
	private static final String[] TELEPORTS = {"1,2 -> 1,6","2,5->2,4"};
	private static final String BASE_URL = "http://localhost:8066";
	
	@Before()
	public void setup(){
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		template.setMap(MAP);
		template.setATeleportTiles(TELEPORTS);
		server = new ServletServer();
		server.add(template);
		server.add(new Player("test", "secret1"));
		client = new TestingClient("secret1", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void testInitialValues() {
		Player player = server.getPlayer("secret1");
		Map map = server.getGame(player).getMap();
		assertTrue("Failed TeleportTile mappings", map.get(1, 2) instanceof TeleportTile);
		assertTrue("Failed TeleportTile mappings", map.get(1, 6) instanceof TeleportTile);
		assertTrue("Failed TeleportTile mappings", map.get(2, 5) instanceof TeleportTile);
		assertTrue("Failed TeleportTile mappings", map.get(2, 4) instanceof TeleportTile);
		
		
		TeleportTile teleportTile  = (TeleportTile)map.get(1,2);
		TeleportTile teleportTile2 = (TeleportTile)map.get(1,6);
		TeleportTile teleportTile3  = (TeleportTile)map.get(2,5);
		TeleportTile teleportTile4 = (TeleportTile)map.get(2,4);
		
		//Test for correct matching destinations.
		assertEquals("FAILED teleportTile connections",map.getPosition(teleportTile2),teleportTile.getDestinationPos());
		assertEquals("FAILED teleportTile connections",map.getPosition(teleportTile),teleportTile2.getDestinationPos());
		assertEquals("FAILED teleportTile connections",map.getPosition(teleportTile4),teleportTile3.getDestinationPos());
		assertEquals("FAILED teleportTile connections",map.getPosition(teleportTile3),teleportTile4.getDestinationPos());
		assertFalse("FAILED teleportTile connections",teleportTile.getDestinationPos() == map.getPosition(teleportTile3)); //Might need to change this to .equals
		
		//teleportTile.getMountedTile().
		//Tests for correct Mounted Tiles.a
		//System.out.println(teleportTile.getMountedTile().toString());
		//assertEquals("FAILEED mountedTile association",(teleportTile.getMountedTile()==(baseMap.get(map.getPosition(teleportTile)))),true);
		//assertEquals("FAILEED mountedTile association",teleportTile2.getMountedTile(),baseMap.get(map.getPosition(teleportTile2)));
		//assertEquals("FAILEED mountedTile association",teleportTile3.getMountedTile(),baseMap.get(map.getPosition(teleportTile3)));
		//assertEquals("FAILEED mountedTile association",teleportTile4.getMountedTile(),baseMap.get(map.getPosition(teleportTile4)));
		
		
	}
	
	@Test
	public void testTeleportActiviation(){
		Player player = server.getPlayer("secret1");
		Game game = server.getGame(player);
		Unit unit = game.getUnit(player);
		Map map = game.getMap();
		TeleportTile teleportTile = (TeleportTile)map.get(1, 2);
		TeleportTile teleportTile2 = (TeleportTile)map.get(1, 6);
		//Checks that the FAILED command works
		assertEquals("Fail command doesnt work","FAILED: Unit not on teleport tile",client.teleport("activate").trim());
		
		//Checks activation.
		unit.setPosition(teleportTile2.getDestinationPos());
		assertEquals("The view was not changed correctly","www\nbt.\n...",client.view().trim());
		client.teleport("activate");
		assertEquals("Teleport Activation failed","www\n.tw\ntbw", client.view().trim());
		client.teleport("activate");
		assertEquals("Return teleportation failed","www\nbt.\n...",client.view().trim());
		
		//Tests if the failed output is triggered if a unit is on the destination tile.
		Player newplayer = new Player("newplayer","secret2");
		server.add(newplayer);
		System.out.println(game.add(newplayer));
		Unit unit2 = game.getUnit(server.getPlayer("secret2"));
		
		unit2.setPosition(teleportTile.getDestinationPos());
		assertEquals("Return teleportation failed","FAILED: Destination currrently blocked",client.teleport("activate").trim());
	}
	
	@Test
	public void testPickingUpTeleportTiles(){
		Player player = server.getPlayer("secret1");
		Game game = server.getGame(player);
		Unit unit = game.getUnit(player);
		Map map = game.getMap();
		assertTrue(map.get(1,2) instanceof TeleportTile);
		TeleportTile teleportTile = (TeleportTile) map.get(1, 2);
		assertTrue(map.get(1,6) instanceof TeleportTile);
		TeleportTile teleportTile2 = (TeleportTile) map.get(1, 6);
		
		//Test fail comamnd
		assertEquals("Failure command doesn't work", "FAILED: Unit not on teleport tile", client.teleport("grab").trim());
		//Testing that Advantageous tiles don't auto teleport.
		moveAndAssert(Direction.EAST, "www\nbt.\n...\n");
		
		//Testing TeleportTile variable changes
		assertEquals("should have failed from lack of score", "FAILED: You can not affort to move this teleport", client.teleport("grab").trim());
		player.setScore(10);
		assertEquals("grab either fails or String output is wrong", "OK: Teleport grabbed", client.teleport("grab").trim());
		assertEquals("grabbing the teleport should have removed score from the player", 10 - AdvTeleportService.COST, player.getScore());
		assertEquals("When picked up the view is not changed correctly","www\nbc.\n...",client.view().trim());
		assertEquals("unit does not contain the teleport tile",teleportTile, unit.getTeleportTile());
		assertNull("When picked up the destination's desintaiton pos is not null",teleportTile2.getDestinationPos());
		assertNull("When picked up the mountedTile is not null",teleportTile.getMountedTile());
		
		//Test that a unit can't hold more than 1 teleport tile;
		unit.setPosition(teleportTile.getDestinationPos());
		assertEquals("destination Position is not correct after grab",client.view().trim(),"www\n.tw\ntbw");
		assertEquals("Grab succeeds when it shouldn't",client.teleport("grab").trim(),"FAILED: Already carrying a teleport tile");
		assertEquals(unit.getTeleportTile(), teleportTile);
		
		//Testing for if another player has the destination teleporter
		Player newplayer = new Player("newplayer","secret2");
		server.add(newplayer);
		System.out.println(game.add(newplayer));
		Unit unit2 = game.getUnit(server.getPlayer("secret2"));
		client2 = new TestingClient("secret2", BASE_URL);
		
		unit2.setPosition(teleportTile.getDestinationPos());
		assertEquals(client2.teleport("grab").trim(),"FAILED: Destination teleport tile being held");
		assertEquals(unit2.getTeleportTile(), null);
	}
	
	@Test
	public void testDroppingTeleportTiles(){
		Player player = server.getPlayer("secret1");
		Game game = server.getGame(player);
		Unit unit = game.getUnit(player);
		Map map = game.getMap();
		TeleportTile teleportTile = (TeleportTile) map.get(1, 2);
		TeleportTile teleportTile2 = (TeleportTile) map.get(1, 6);
		player.setScore(10);
		
		assertEquals("Failure command doesn't work", "FAILED: not carrying a teleport tile",client.teleport("drop").trim());
		client.move(Direction.EAST);
		assertEquals("OK: Teleport grabbed",client.teleport("grab").trim());		
		assertNull("destination's destination does not change to null on grab", teleportTile2.getDestinationPos());
		
		//Check you cant drop on another teleport tile		
		unit.setPosition(teleportTile.getDestinationPos());
		assertEquals("drop succeeds when it should fail","FAILED: Can't drop a teleport tile on a teleport tile", client.teleport("drop").trim());
		
		//Test for regular drop.
		unit.setPosition(1,2);
		assertEquals("The view was not changed correctly","www\nbc.\n...",client.view().trim());
		Tile baseTile = map.get(unit.getPosition());
		assertEquals("either the output is incorrect or the drop failed","OK: Teleport dropped",client.teleport("drop").trim());
		assertNull("teleport tile was not removed from the unit", unit.getTeleportTile());
		assertEquals("The view was not changed correctly","www\nbt.\n...",client.view().trim());
	
		//Check attributes
		assertEquals("attributes dont match up", baseTile,teleportTile.getMountedTile());
		assertEquals("destination tile's destination does not match", map.getPosition(teleportTile), teleportTile2.getDestinationPos());
	}
	
	private void moveAndAssert(Direction d, String expected){moveAndAssert(d, expected, true);}
	private void moveAndAssert(Direction d, String expected, boolean success) {
		assertEquals((success ? "OK" : "FAILED"), client.move(d).trim());
		assertEquals(expected.trim(), client.view().trim());
	}
}
