package com.golddigger;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.golddigger.core.AppContext;
import com.golddigger.model.BlankMap;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.MapPrinter;

public class DelayedServerTest {
	private static final String MAP = "wwwww\nw.b.w\nwwwww";
	private static final long DELAY = 1000;

	@Test
	public void test() throws InterruptedException {
		DelayedServer server;
		String context = "delay1"; 
		server = new DelayedServer(context, DELAY);
		server.start();
		server.getContext().add(new TestGameTemplate(MAP));
		Player player = new Player("test", "secret");
		server.getContext().add(player);
		Unit unit = server.getContext().getGame(player).getUnit(player);
		
		System.out.println(MapPrinter.print(server.getContext().getGame(player).getMap()));
		assertEquals(1, unit.getX());
		assertEquals(2, unit.getY());
		System.out.println(unit.getPosition());

		server.add(System.currentTimeMillis()+",localhost:8066/golddigger/digger/test/move/west");
		Thread.sleep(2*DELAY);
		System.out.println(unit.getPosition());
		assertEquals(1,unit.getY());

		
	}

}
