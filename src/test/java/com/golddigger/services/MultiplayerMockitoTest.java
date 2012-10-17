package com.golddigger.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.utils.TestWriter;

@RunWith(MockitoJUnitRunner.class)
public class MultiplayerMockitoTest {
	private static final String URL = "http://localhost/golddigger/digger/test/view";
	private static final int START = 100, DURATION = 150, END = 100;
	private MultiplayerService service;
	private TestWriter writer;
	@Mock private Game game;
	@Mock private Player player;
	@Mock private Map map;
	
	@Before
	public void before(){
		service = new MultiplayerService(START, DURATION, END);
		service.setGame(game);
		writer = new TestWriter();
		
		when(player.getName()).thenReturn("test");
		when(game.getPlayer("test")).thenReturn(player);
		when(game.getMap()).thenReturn(map);
		when(map.hasGoldLeft()).thenReturn(true);
	}
	
	@Test
	public void testStarting(){
		boolean runnable = service.runnable(URL);
		boolean consumed = service.execute(URL, writer.getPrintWriter());
		
		String response = writer.getHistory();
		
		assertTrue("wasn't runnable",runnable);
		assertTrue("wasn't consumed",consumed);
		assertTrue("state was not starting - response:"+response, response.startsWith("state: starting,"));
		assertTrue("did not fail the request", response.contains("FAILED"));
	}
	
	@Test
	public void testRunning() throws InterruptedException{
		when(map.getTiles()).thenReturn(new Tile[][]{{}});
		when(game.getBases()).thenReturn(new BaseTile[]{});
		
		long start = System.currentTimeMillis();
		Thread.sleep(START+1);
		System.out.println("slept for: " + (System.currentTimeMillis() - start)+"ms");
		boolean runnable = service.runnable(URL);
		boolean consumed = service.execute(URL, writer.getPrintWriter());
		
		String response = writer.getHistory();
		
		assertTrue("wasn't runnable",runnable);
		assertFalse("wasn't consumed", consumed);
		assertTrue("state was not running - response:"+response, response.startsWith("state: running,"));
	}
	
	@Test
	public void testEnding_TimeUp() throws InterruptedException{
		when(map.getTiles()).thenReturn(new Tile[][]{{}});
		when(game.getBases()).thenReturn(new BaseTile[]{});

		long start = System.currentTimeMillis();
		Thread.sleep(START+DURATION+1);
		System.out.println("slept for: " + (System.currentTimeMillis() - start)+"ms");
		
		boolean runnable = service.runnable(URL);
		boolean consumed = service.execute(URL, writer.getPrintWriter());
		
		String response = writer.getHistory();

		assertTrue("wasn't runnable",runnable);
		assertFalse("wasn't consumed", consumed);
		assertTrue("state was not ending - response:"+response, response.startsWith("state: ending,"));
	}
	
	@Test
	public void testEnding_NoGold() throws InterruptedException{
		when(map.getTiles()).thenReturn(new Tile[][]{{}});
		when(game.getBases()).thenReturn(new BaseTile[]{});
		when(game.getMap().hasGoldLeft()).thenReturn(false);

		long start = System.currentTimeMillis();
		Thread.sleep(START+DURATION+1);
		System.out.println("slept for: " + (System.currentTimeMillis() - start)+"ms");
		
		boolean runnable = service.runnable(URL);
		boolean consumed = service.execute(URL, writer.getPrintWriter());
		
		String response = writer.getHistory();

		assertTrue("wasn't runnable",runnable);
		assertFalse("wasn't consumed", consumed);
		assertTrue("state was not ending - response:"+response, response.startsWith("state: ending,"));
		
	}
	
	@Test
	public void testEnded() throws InterruptedException{
		when(map.getTiles()).thenReturn(new Tile[][]{{}});
		when(game.getUnits()).thenReturn(new Unit[]{});
		when(game.getBases()).thenReturn(new BaseTile[]{});

		long start = System.currentTimeMillis();
		Thread.sleep(START+DURATION+END+1);
		System.out.println("slept for: " + (System.currentTimeMillis() - start)+"ms");
		
		boolean runnable = service.runnable(URL);
		boolean consumed = service.execute(URL, writer.getPrintWriter());
		
		String response = writer.getHistory();
		
		assertTrue("wasn't runnable",runnable);
		assertTrue("wasn't consumed", consumed);
		assertTrue("state was not finished - response:"+response, response.startsWith("state: finished,"));
		assertTrue("did not fail the command", response.contains("FAILED"));
	}
}
