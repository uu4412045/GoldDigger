package com.golddigger;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.golddigger.client.TestingClient;
import com.golddigger.core.Service;
import com.golddigger.core.ServiceGenerator;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.server.TestServer;
import com.golddigger.server.delayed.DelayedServer;
import com.golddigger.server.delayed.DirectInputDelayedServer;
import com.golddigger.server.delayed.FileDelayedServer;
import com.golddigger.services.LogService;
import com.golddigger.services.MoveService;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.services.ViewService;
import com.golddigger.templates.TestGameTemplate;

public class DelayedServerTest {
	private static final String MAP = "wwwww\nw.b.w\nwwwww";
	private static final long SECOND = 1000;
	private static final String BASE_URL = "http://localhost:8066";

	@Test
	public void testDirectInputDelayedServer() throws InterruptedException {
		DirectInputDelayedServer server;
		String context = "delay1"; 
		server = new DirectInputDelayedServer(context, SECOND);
		server.start();
		server.getContext().add(new TestGameTemplate(MAP));
		Player player = new Player("test", "secret");
		server.getContext().add(player);
		Unit unit = server.getContext().getGame(player).getUnit(player);
		
		assertEquals(1, unit.getX());
		assertEquals(2, unit.getY());
		server.add(DelayedServer.buildEntry("localhost:8066/golddigger/digger/test/move/west"));
		Thread.sleep(SECOND*2);
		assertEquals(1,unit.getY());
	}
}
