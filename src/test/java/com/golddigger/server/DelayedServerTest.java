package com.golddigger.server;

import static org.junit.Assert.*;

import org.junit.Test;

import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Coordinate;
import com.golddigger.model.Unit;
import com.golddigger.services.NextService;
import com.golddigger.templates.TestGameTemplate;

public class DelayedServerTest {

	@Test
	public void testDelay() throws InterruptedException{
		GoldDiggerServer server = new GoldDiggerServer();
		DirectInputDelayedServer delay = new DirectInputDelayedServer(server, 3000);
		Player player = new Player("test", "secret");
		
		server.add(new TestGameTemplate("wwwww\nw.b.w\nwwwww"));
		server.add(new NextService());
		server.add(player);
		
		Game game = server.getGame(player);
		Unit unit = game.getUnit(player);
		Coordinate before = unit.getPosition();
		Direction east = Direction.EAST;
		
		delay.add("http://localhost:8066/golddigger/digger/secret/move/"+east.toString());
		assertEquals(before, unit.getPosition());
		
		Thread.sleep(4000);
		assertEquals(east.getOffset(before), unit.getPosition());
	}
	
	@Test
	public void testBuildEntry() {
		String url = "http://localhost:8066/golddigger/digger/secret/move/south";
		String[] entry = DelayedServer.buildEntry(url).split(",");
		assertEquals(System.currentTimeMillis(), Long.parseLong(entry[0]), 30);
		assertEquals(url+"\n", entry[1]);
	}

}
