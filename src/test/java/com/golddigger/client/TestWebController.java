package com.golddigger.client;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.templates.CustomizableGameTemplate;
import com.golddigger.templates.TestGameTemplate;

public class TestWebController {
    private GenericServer server;
    private WebController controller;
    private final String PLAYER_SECRET = "secret";
    
    @Before
    public void startServer() throws Exception {
        server = new GenericServer();
        controller = new WebController(PLAYER_SECRET, "localhost:8066", 100);
    }
    
    @After
    public void stopServer() throws Exception {
        server.stop();
    }
    
    @Test
    public void view() throws IOException {
    	server.addTemplate(new TestGameTemplate("www\nwbw\nwww"));
    	server.addPlayer("name", PLAYER_SECRET);
        assertEquals("www\nwbw\nwww", controller.view().getText().trim());
    }

    @Test
    public void score() throws IOException {
    	server.addTemplate(new TestGameTemplate("wwww\nwb2w\nwwww"));
    	server.addPlayer("name", PLAYER_SECRET);
        assertEquals("0", controller.score().getText().trim());
    }

    @Test
    public void grab() throws IOException {
    	server.addTemplate(new TestGameTemplate("wwww\nwb2w\nwwww\n"));
    	server.addPlayer("name", PLAYER_SECRET);
    	
    	assertEquals("0", controller.grab().getText().trim());
    	assertEquals("0", controller.drop().getText().trim());
    	
    	assertEquals("OK", controller.moveEast().getText().trim());
    	assertEquals("2", controller.grab().getText().trim());
    	assertEquals("2", controller.drop().getText().trim());
    }

    @Test
    public void moveToTheNextField() throws IOException {
    	server.addTemplate(new TestGameTemplate("www\nwbw\nwww"));
    	server.addTemplate(new TestGameTemplate("www\nwbw\nw2w\nwww"));
    	server.addPlayer("name", PLAYER_SECRET);
    	
    	assertEquals("www\nwbw\nwww", controller.view().getText().trim());
    	assertEquals("OK", controller.nextField().getText().trim());
    	assertEquals("www\nwbw\nw2w", controller.view().getText().trim());
    }

    @Test
    public void moveSquare() throws IOException {
    	server.addTemplate(new TestGameTemplate("wwww\nwb.w\nw..w\nwwww"));
    	server.addPlayer("name", PLAYER_SECRET);
    	
    	assertEquals("OK", controller.moveEast().getText().trim());
    	assertEquals("FAILED", controller.moveEast().getText().trim());
    	assertEquals("www\nb.w\n..w", controller.view().getText().trim());
    	
    	assertEquals("OK", controller.moveSouth().getText().trim());
    	assertEquals("FAILED", controller.moveSouth().getText().trim());
    	assertEquals("b.w\n..w\nwww", controller.view().getText().trim());

    	assertEquals("OK", controller.moveWest().getText().trim());
    	assertEquals("FAILED", controller.moveWest().getText().trim());
    	assertEquals("wb.\nw..\nwww", controller.view().getText().trim());

    	assertEquals("OK", controller.moveNorth().getText().trim());
    	assertEquals("FAILED", controller.moveNorth().getText().trim());
    	assertEquals("www\nwb.\nw..", controller.view().getText().trim());
    }
    
    @Test
    public void moveHex() throws IOException {
    	CustomizableGameTemplate template = new CustomizableGameTemplate();
    	template.setMap("wwwww\nw...w\nw.b.w\nww.ww\nwwwww");
    	template.setNumberOfSides(6);
    	server.addTemplate(template);
    	server.addPlayer("name", PLAYER_SECRET);

    	assertEquals("OK", controller.moveNorth().getText().trim());
    	assertEquals("FAILED", controller.moveNorth().getText().trim());
    	assertEquals("www\n...\n?b?", controller.view().getText().trim());
    	
    	assertEquals("OK", controller.moveSouthEast().getText().trim());
    	assertEquals("FAILED", controller.moveSouthEast().getText().trim());
    	assertEquals("?w?\n..w\nb.w", controller.view().getText().trim());

    	assertEquals("OK", controller.moveSouth().getText().trim());
    	assertEquals("FAILED", controller.moveSouth().getText().trim());
    	assertEquals("?.?\nb.w\n.ww", controller.view().getText().trim());

    	assertEquals("OK", controller.moveSouthWest().getText().trim());
    	assertEquals("FAILED", controller.moveSouthWest().getText().trim());
    	assertEquals(".b.\nw.w\n?w?", controller.view().getText().trim());
    	
    	assertEquals("OK", controller.moveNorthWest().getText().trim());
    	assertEquals("FAILED", controller.moveNorthWest().getText().trim());
    	assertEquals("?.?\nw.b\nww.", controller.view().getText().trim());
    	
    	assertEquals("OK", controller.moveNorth().getText().trim());

    	assertEquals("OK", controller.moveNorthEast().getText().trim());
    	assertEquals("FAILED", controller.moveNorthEast().getText().trim());
    	assertEquals("www\n...\n?b?", controller.view().getText().trim());
    }
    
    @Test
    public void testCannon() throws IOException {
    	CustomizableGameTemplate template = new CustomizableGameTemplate();
    	template.setMap("wwwww\nw...w\nw.b.w\nw...w\nwwwww");
    	template.enableCannons(true);
    	server.addTemplate(template);
    	server.addPlayer("name", PLAYER_SECRET);

    	assertEquals("FAILED: Dont have enough cash", controller.cannonBuy().getText().trim());
    	assertEquals("FAILED: out of ammo", controller.cannonShoot(0,1).getText().trim());
    }
}
