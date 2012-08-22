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
    	// In each test you need to configure the template
    	// add the template
    	// add a player with the name "test"
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
        assertEquals("...\n.b.\n...\n", client.view());
        
        client.move(Direction.NORTH);
        assertEquals("www\n...\n.b.\n", client.view());
        
        client.move(Direction.EAST);
        assertEquals("www\n..w\nb.w\n", client.view());
        
        client.move(Direction.SOUTH);
        assertEquals("..w\nb.w\n..w\n", client.view());
        
        client.move(Direction.SOUTH);
        assertEquals("b.w\n..w\nwww\n", client.view());
        
        client.move(Direction.WEST);
        assertEquals(".b.\n...\nwww\n", client.view());
        
        client.move(Direction.WEST);
        assertEquals("w.b\nw..\nwww\n", client.view());
        
        client.move(Direction.NORTH);
        assertEquals("w..\nw.b\nw..\n", client.view());
        
        client.move(Direction.NORTH);
        assertEquals("www\nw..\nw.b\n", client.view());
    }
    
    @Test
    public void squareViewLOS2() throws Exception {
    	template.setLineOfSight(2);
    	server.addTemplate(template);
    	server.addPlayer(name, "secret");
        assertEquals("wwwww\nw...w\nw.b.w\nw...w\nwwwww\n", client.view());
        
        client.move(Direction.NORTH);
        assertEquals("-----\nwwwww\nw...w\nw.b.w\nw...w\n", client.view());
        
        client.move(Direction.EAST);
        assertEquals("-----\nwwww-\n...w-\n.b.w-\n...w-\n", client.view());
        
        client.move(Direction.SOUTH);
        assertEquals("wwww-\n...w-\n.b.w-\n...w-\nwwww-\n", client.view());
        
        client.move(Direction.SOUTH);
        assertEquals("...w-\n.b.w-\n...w-\nwwww-\n-----\n", client.view());
        
        client.move(Direction.WEST);
        assertEquals("w...w\nw.b.w\nw...w\nwwwww\n-----\n", client.view());
        
        client.move(Direction.WEST);
        assertEquals("-w...\n-w.b.\n-w...\n-wwww\n-----\n", client.view());
        
        client.move(Direction.NORTH);
        assertEquals("-wwww\n-w...\n-w.b.\n-w...\n-wwww\n", client.view());
        
        client.move(Direction.NORTH);
        assertEquals("-----\n-wwww\n-w...\n-w.b.\n-w...\n", client.view());
    }
    
    @Test
    public void hexViewLOS1(){
    	template.setNumberOfSides(6);
    	server.addTemplate(template);
    	server.addPlayer(name, "secret");
    	
        assertEquals( "...\n.b.\n?.?\n", client.view());
        
        client.move(Direction.NORTH);
        assertEquals( "www\n...\n?b?\n", client.view());
        
        client.move(Direction.SOUTH_EAST);
        assertEquals("?w?\n..w\nb.w\n", client.view());
        
        client.move(Direction.SOUTH);
    	assertEquals("?.?\nb.w\n..w\n", client.view());
    	
    	client.move(Direction.SOUTH_WEST);
    	assertEquals(".b.\n...\n?w?\n", client.view());
    	
    	client.move(Direction.NORTH_WEST);
    	assertEquals("?.?\nw.b\nw..\n", client.view());
    	
    	client.move(Direction.NORTH);
    	assertEquals("?w?\nw..\nw.b\n", client.view());
    } 
    
    @Test
    public void hexViewLOS2(){
    	template.setNumberOfSides(6);
    	template.setLineOfSight(2);
    	server.addTemplate(template);
    	server.addPlayer(name, "secret");

        assertEquals( "?www?\nw...w\nw.b.w\nw...w\n??w??\n" ,client.view());
        
        client.move(Direction.NORTH);
        assertEquals( "-----\nwwwww\nw...w\nw.b.w\n??.??\n",client.view());
        
        client.move(Direction.SOUTH_EAST);
        assertEquals( "-----\nwwww-\n...w-\n.b.w-\n?..w-\n",client.view());
        
        client.move(Direction.SOUTH);
        assertEquals( "??w?-\n...w-\n.b.w-\n...w-\n?www-\n",client.view());
        
        client.move(Direction.SOUTH_WEST);
        assertEquals("?...?\nw.b.w\nw...w\nwwwww\n-----\n", client.view());
        
        client.move(Direction.NORTH_WEST);
        assertEquals("-?w??\n-w...\n-w.b.\n-w...\n-www?\n", client.view());
        
        client.move(Direction.NORTH);
        assertEquals("-----\n-wwww\n-w...\n-w.b.\n-w..?\n", client.view());
    }
}
