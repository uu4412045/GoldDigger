package com.golddigger.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.golddigger.model.EmptyMap;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.utils.TestWriter;

@RunWith(MockitoJUnitRunner.class)
public class HexViewServiceTest {
	@Spy Game game= new Game(0);
	TestWriter writer;
	HexViewService service;
	Player player = new Player("test", "secret");
	private final String URL = "http://server/golddigger/digger/"+player.getName()+"/view";
	
	@Before
	public void before(){
		writer = new TestWriter();
		service = new HexViewService();
		game.add(service);
	}
	
	@Test
	public void lineOfSight1() {
		final int los = 1;
		game.setMap(new EmptyMap(3,3,1,1));
		game.add(player);
		final String expected = "?.?\n"+
								".b.\n"+
								"...\n";
		assertview(los, expected);
	}
	
	@Test
	public void offsetLineOfSight1() {
		final int los = 1;
		game.setMap(new EmptyMap(3,4,1,2));
		game.add(player);
		final String expected = "...\n"+
								".b.\n"+
								"?.?\n";
		assertview(los, expected);
	}
	
	@Test
	public void lineOfSight2() {
		final int los = 2;
		game.setMap(new EmptyMap(5,5,2,2));
		game.add(player);
		final String expected = "?...?\n"+
								".....\n"+
								"..b..\n"+
								".....\n"+
								"??.??\n";
		assertview(los, expected);
	}
	
	@Test
	public void offsetLineOfSight2() {
		final int los = 2;
		game.setMap(new EmptyMap(5,6,2,3));
		game.add(player);
		final String expected = "??.??\n"+
								".....\n"+
								"..b..\n"+
								".....\n"+
								"?...?\n";
		assertview(los, expected);
	}
	
	@Test
	public void lineOfSight3(){
		final int los = 3;
		game.setMap(new EmptyMap(7,7,3,3));
		game.add(player);
		final String expected = "???.???\n"+
								"?.....?\n"+
								".......\n"+
								"...b...\n"+
								".......\n"+
								".......\n"+
								"??...??\n";
		assertview(los, expected);
	}
	
	@Test
	public void offsetLineOfSight3(){
		final int los = 3;
		game.setMap(new EmptyMap(7,8,3,4));
		game.add(player);
		final String expected = "??...??\n"+
								".......\n"+
								".......\n"+
								"...b...\n"+
								".......\n"+
								"?.....?\n"+
								"???.???\n";
		assertview(los, expected);
	}

	private void assertview(int los, String expected){
		service.setLineOfSight(los);
		assertTrue(service.runnable(URL));
		assertTrue(service.execute(URL, writer.getPrintWriter()));
		assertEquals(expected.trim(), writer.getHistory().trim());
	}
}
