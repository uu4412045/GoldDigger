package com.golddigger.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.model.Game;
import com.golddigger.model.Player;

public class DayNightMockitoTest {
	private static final String URL = "http://localhost/golddigger/digger/test/move/";
	private final PrintWriter writer = mock(PrintWriter.class);
	DayNightService service;
	Game game;
	
	@Before
	public void before(){
		game = spy(new Game(0));
		Player player = new Player("test", "secret");
		when(game.getPlayer("test")).thenReturn(player);
	}
	
	@After
	public void after(){
		game = null;
		service = null;
	}
	
	@Test
	public void testSquare() {
		ViewService view = mock(ViewService.class);
		service = new DayNightService(3, 25);
		game.add(view);
		game.add(service);
		when(view.getLineOfSight()).thenReturn(4);
		
		moveAndAssert("north", 3);
		verify(view).setLineOfSight(1);
		moveAndAssert("south", 3);
		verify(view).setLineOfSight(16);
		moveAndAssert("west", 3);
		verify(view, times(2)).setLineOfSight(1);
		moveAndAssert("east", 3);
		verify(view, times(2)).setLineOfSight(16);
	}
	
	@Test
	public void testInvalidSquare(){
		ViewService view = mock(ViewService.class);
		service = new DayNightService(3, 25);
		game.add(view);
		game.add(service);
		when(view.getLineOfSight()).thenReturn(4);

		moveAndAssert("north_east", 3);
		moveAndAssert("north_west", 3);
		moveAndAssert("south_east", 3);
		moveAndAssert("south_west", 3);
		verify(view, never()).setLineOfSight(anyInt());
	}
	
	@Test
	public void testHex(){
		HexViewService view = mock(HexViewService.class);
		service = new DayNightService(3, 25);
		game.add(view);
		game.add(service);
		when(view.getLineOfSight()).thenReturn(4);
		
		moveAndAssert("north", 3);
		verify(view).setLineOfSight(1);
		
		moveAndAssert("south", 3);
		verify(view).setLineOfSight(16);
		
		moveAndAssert("north_east", 3);
		verify(view, times(2)).setLineOfSight(1);
		
		moveAndAssert("north_west", 3);
		verify(view, times(2)).setLineOfSight(16);
		
		moveAndAssert("south_east", 3);
		verify(view, times(3)).setLineOfSight(1);
		
		moveAndAssert("south_west", 3);
		verify(view, times(3)).setLineOfSight(16);
	}
	
	@Test
	public void testInvalidHex(){
		HexViewService view = mock(HexViewService.class);
		service = new DayNightService(3, 25);
		game.add(view);
		game.add(service);
		when(view.getLineOfSight()).thenReturn(4);

		moveAndAssert("east", 3);
		moveAndAssert("west", 3);
		verify(view, never()).setLineOfSight(anyInt());
	}
	
	public void moveAndAssert(String direction, int repeat){
		for (int i = 0; i < repeat; i++){
			assertTrue(service.runnable(URL+direction));
			assertFalse(service.execute(URL+direction, writer));
		}
		verifyZeroInteractions(writer);
	}

}
