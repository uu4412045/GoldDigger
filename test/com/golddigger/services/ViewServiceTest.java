package com.golddigger.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.golddigger.TestServer;
import com.golddigger.client.TestingClient;
import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Map;
import com.golddigger.model.Player;
import com.golddigger.services.MoveService.Direction;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.utils.MapMaker;
import com.meterware.httpunit.WebConversation;

public class ViewServiceTest {
	TestServer server;
	TestingClient client;
	private static final String STRING_MAP_1 = "wwwwww\nw.123w\nw45b6w\nw789ww\nwwwww";
	private static final String STRING_MAP_2 = "wwwww\nw...w\nw.b.w\nw...w\nwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		Map map_1 = MapMaker.parse(STRING_MAP_1);
		List<Service> services_1 = new ArrayList<Service>();
		services_1.add(new MoveService());
		services_1.add(new ViewService());
		Map map_2 = MapMaker.parse(STRING_MAP_2);
		List<Service> services_2 = new ArrayList<Service>();
		services_2.add(new MoveService());
		services_2.add(new ViewService(2));
		server = new TestServer();
		AppContext.add(new TestGameTemplate(map_1, services_1));
		AppContext.add(new TestGameTemplate(map_2, services_2));
		AppContext.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void testView() {
		assertEquals("123\n5b6\n89w",client.view().trim());
		assertEquals("OK",client.move(Direction.WEST).trim());
		assertEquals(".12\n45b\n789",client.view().trim());
		AppContext.progress(AppContext.getPlayer("test"));
		
		//Testing Line Of Sight
		assertEquals("wwwww\nw...w\nw.b.w\nw...w\nwwwww",client.view().trim());
	}
}
