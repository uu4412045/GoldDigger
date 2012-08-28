package com.golddigger;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.client.TestingClient;
import com.golddigger.model.Direction;
import com.golddigger.templates.CustomizableGameTemplate;

public class TestLineOfSight {
	private GenericServer server;
	private TestingClient client;
	private CustomizableGameTemplate template;
	private final String map = "wwwww\nw...w\nw.b.w\nw...w\nwwwww\n";
	private final String name = "test";
        
    @Before
    public void before() throws Exception {
    	server = new GenericServer();
    	client = new TestingClient(name, "http://localhost:8066");
    	template = new CustomizableGameTemplate();
    	template.setMap(map);
    }
    
    @After
    public void after(){
    	server.stop();
    }
    
    // Test the sight view for the sides
    @Test
    public void squareViewLOS1() throws Exception {
    	server.addTemplate(template);
    	server.addPlayer(name, "secret");
        assertEquals("...\n.b.\n...", client.view().trim());
        moveAndAssert(Direction.NORTH, "www\n...\n.b.");
        moveAndAssert(Direction.EAST,  "www\n..w\nb.w");
        moveAndAssert(Direction.SOUTH, "..w\nb.w\n..w");
        moveAndAssert(Direction.SOUTH, "b.w\n..w\nwww");
        moveAndAssert(Direction.WEST,  ".b.\n...\nwww");
        moveAndAssert(Direction.WEST,  "w.b\nw..\nwww");
        moveAndAssert(Direction.NORTH, "w..\nw.b\nw..");
        moveAndAssert(Direction.NORTH, "www\nw..\nw.b");
    }
    
    @Test
    public void squareViewLOS2() throws Exception {
    	template.setLineOfSight(2);
    	server.addTemplate(template);
    	server.addPlayer(name, "secret");
        assertEquals("wwwww\nw...w\nw.b.w\nw...w\nwwwww", client.view().trim());
        moveAndAssert(Direction.NORTH, "-----\nwwwww\nw...w\nw.b.w\nw...w");
        moveAndAssert(Direction.EAST,  "-----\nwwww-\n...w-\n.b.w-\n...w-");
        moveAndAssert(Direction.SOUTH, "wwww-\n...w-\n.b.w-\n...w-\nwwww-");
        moveAndAssert(Direction.SOUTH, "...w-\n.b.w-\n...w-\nwwww-\n-----");
        moveAndAssert(Direction.WEST,  "w...w\nw.b.w\nw...w\nwwwww\n-----");
        moveAndAssert(Direction.WEST,  "-w...\n-w.b.\n-w...\n-wwww\n-----");
        moveAndAssert(Direction.NORTH, "-wwww\n-w...\n-w.b.\n-w...\n-wwww");
        moveAndAssert(Direction.NORTH, "-----\n-wwww\n-w...\n-w.b.\n-w...");
    }
    
    @Test
    public void hexViewLOS1(){
    	template.setNumberOfSides(6);
    	server.addTemplate(template);
    	server.addPlayer(name, "secret");
    	
        assertEquals( "...\n.b.\n?.?", client.view().trim());
        moveAndAssert(Direction.NORTH,      "www\n...\n?b?");
        moveAndAssert(Direction.SOUTH_EAST, "?w?\n..w\nb.w");
        moveAndAssert(Direction.SOUTH,      "?.?\nb.w\n..w");
    	moveAndAssert(Direction.SOUTH_WEST, ".b.\n...\n?w?");
    	moveAndAssert(Direction.NORTH_WEST, "?.?\nw.b\nw..");
    	moveAndAssert(Direction.NORTH,      "?w?\nw..\nw.b");
    } 
    
    @Test
    public void hexViewLOS2(){
    	template.setNumberOfSides(6);
    	template.setLineOfSight(2);
    	server.addTemplate(template);
    	server.addPlayer(name, "secret");

        assertEquals( "?www?\nw...w\nw.b.w\nw...w\n??w??", client.view().trim());
        moveAndAssert(Direction.NORTH,      "-----\nwwwww\nw...w\nw.b.w\n??.??");
        moveAndAssert(Direction.SOUTH_EAST, "-----\nwwww-\n...w-\n.b.w-\n?..w-");
        moveAndAssert(Direction.SOUTH,      "??w?-\n...w-\n.b.w-\n...w-\n?www-");
        moveAndAssert(Direction.SOUTH_WEST, "?...?\nw.b.w\nw...w\nwwwww\n-----");
        moveAndAssert(Direction.NORTH_WEST, "-?w??\n-w...\n-w.b.\n-w...\n-www?");
        moveAndAssert(Direction.NORTH,      "-----\n-wwww\n-w...\n-w.b.\n-w..?");
    }

	private void moveAndAssert(Direction direction, String expected) {
		client.move(direction);        
		assertEquals( expected.trim(), client.view().trim());
	}
}
