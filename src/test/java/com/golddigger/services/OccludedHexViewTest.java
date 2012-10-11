package com.golddigger.services;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import com.golddigger.model.BlankMap;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.utils.MapMaker;
import com.golddigger.utils.TestWriter;

public class OccludedHexViewTest {
	private static final String VIEW_URL = "http://localhost/golddigger/digger/test/view";
	private static final String MAP =
					"wwwwwwwww\n" +
					"wb......w\n"+
					"w.......w\n"+
					"w.......w\n"+
					"w...w...w\n"+
					"w.......w\n"+
					"w.......w\n"+
					"w.......w\n"+
					"wwwwwwwww\n";

	@Spy Game game = new Game(0);
	Player player;
	TestWriter writer = new TestWriter();
	OccludedHexViewService service;

	@Before
	public void before() throws Exception{
		player = new Player("test", "secret");
		service = new OccludedHexViewService(4);
		game.setMap(MapMaker.parse(MAP));
		game.add(player);
		game.add(service);
	}
	
	@Test
	public void occludeLOS4() {
		service.setLineOfSight(4);
		Unit unit = game.getUnit(player);
		
		unit.setPosition(3,4);
		assertView("---------\n?wwwwwww?\nwb......w\nw.......w\nw.......w\nw...w...w\nw..???..w\n??.???.??\n?????????\n");
		unit.setPosition(2,3);
		assertView("---------\n---------\n-?wwwww?w\n-wb......\n-w.......\n-w.......\n-w...w?..\n-w...????\n-??..????\n");
		unit.setPosition(3,3);
		assertView("---------\n-?wwwww??\n-wb......\n-w.......\n-w.......\n-w...w???\n-w....???\n-w.....??\n-??...???\n");
		unit.setPosition(4,2);
		assertView("--?www???\n--wb....?\n--w......\n--w....??\n--w...w??\n--w.....?\n--w......\n--w....??\n--??w????\n");
		unit.setPosition(4,3);
		assertView("-???w????\n-?b....??\n-w....???\n-w....???\n-w...w.?.\n-w.......\n-w.......\n-w......?\n-??www???\n");
		unit.setPosition(5,3);
		assertView("-???.????\n-?...????\n-w...???.\n-w...w...\n-w.......\n-w.......\n-w.......\n-wwwwwww?\n---------\n");
		unit.setPosition(5,4);
		assertView("?????????\n?..???..?\nw...?...w\nw...w...w\nw.......w\nw.......w\nw.......w\n??wwwww??\n---------\n");
		unit.setPosition(5,5);
		assertView("????.???-\n????...?-\n.???...w-\n...w...w-\n.......w-\n.......w-\n.......w-\n?wwwwwww-\n---------\n");
		unit.setPosition(4,5);
		assertView("????w???-\n??.....?-\n???....w-\n???....w-\n.?.w...w-\n.......w-\n.......w-\n?......w-\n???www??-\n");
		unit.setPosition(4,6);
		assertView("???www?--\n?.....w--\n......w--\n??....w--\n??w...w--\n?.....w--\n......w--\n??....w--\n????w??--\n");
		unit.setPosition(3,5);
		assertView("---------\n??wwwww?-\nb......w-\n.......w-\n.......w-\n???w...w-\n???....w-\n??.....w-\n???...??-\n");
		unit.setPosition(2,5);
		assertView("---------\n---------\nw?wwwww?-\nb......w-\n.......w-\n.......w-\n..?w...w-\n????...w-\n????..??-\n");
	}
	
	public void assertView(String expected){
		assertTrue(service.runnable(VIEW_URL));
		assertTrue(service.execute(VIEW_URL, writer.getPrintWriter()));
		assertEquals(expected.trim(), writer.getHistory().trim());
		writer.clear(); //clear the history for the next assertView
	}
}
