package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.ServletServer;
import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.model.Player;
import com.golddigger.templates.CustomizableGameTemplate;

public class HumanHexViewLOS1Test {
	ServletServer server;
	TestingClient client;
	private static final String MAP = "wwwww\nw...w\nw.b.w\nw...w\nwwwww\n";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		template.setMap(MAP);
		template.setNumberOfSides(6);
		template.setLineOfSight(1);
		
		server = new ServletServer();
		server.add(template);
		server.add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}

	@After()
	public void halt(){
		server.stop();
	}
	
	@Test
	public void test() {
		
		// starts at the base at position (2, 2) and traverses every tile
		assertEquals(	"---\n" +
						" . \n" +
						". .\n" +
						" b \n" +
						". .\n" +
						" . \n" +
						"? ?", client.humanView().trim());
		
		client.move(Direction.NORTH);
		assertEquals(	"---\n" +
						" w \n" +
  			  			"w w\n" +
  			  			" . \n" +
  			  			". .\n" +
  			  			" b \n" +
  			  			"? ?" , client.humanView().trim());
		
		client.move(Direction.SOUTH_EAST);
		assertEquals(	"? ?\n" +
	  			  		" w \n" +
	  			  		". w\n" +
	  			  		" . \n" +
	  			  		"b w\n" +
	  			  		" . \n" + 
	  			  		"---" , client.humanView().trim());
		
		client.move(Direction.SOUTH);
		assertEquals(	"? ?\n" +
	  			  		" . \n" +
	  			  		"b w\n" +
	  			  		" . \n" +
	  			  		". w\n" +
	  			  		" . \n" + 
	  			  		"---" , client.humanView().trim());
		
		client.move(Direction.SOUTH);
		assertEquals(	"? ?\n" +
	  			  		" . \n" +
	  			  		". w\n" +
	  			  		" . \n" +
	  			  		"w w\n" +
	  			  		" w \n" + 
	  			  		"---" , client.humanView().trim());
		
		client.move(Direction.NORTH_WEST);
		assertEquals(	"---\n" +
						" b \n" +
						". .\n" +
  			  			" . \n" +
  			  			". .\n" +
  			  			" w \n" +
  			  			"? ?", client.humanView().trim());
		
		client.move(Direction.SOUTH_WEST);
		assertEquals(	"? ?\n" +
	  			  		" . \n" +
	  			  		"w .\n" +
	  			  		" . \n" +
	  			  		"w w\n" +
	  			  		" w \n" + 
	  			  		"---" , client.humanView().trim());
		
		client.move(Direction.NORTH);
		assertEquals(	"? ?\n" +
	  			  		" . \n" +
	  			  		"w b\n" +
	  			  		" . \n" +
	  			  		"w .\n" +
	  			  		" . \n" + 
	  			  		"---" , client.humanView().trim());
		
		client.move(Direction.NORTH);
		assertEquals(	"? ?\n" +
	  			  		" w \n" +
	  			  		"w .\n" +
	  			  		" . \n" +
	  			  		"w b\n" +
	  			  		" . \n" + 
	  			  		"---" , client.humanView().trim());				
	}

}
