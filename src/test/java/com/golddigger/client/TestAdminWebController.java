package com.golddigger.client;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.templates.TestGameTemplate;
import com.meterware.httpunit.WebResponse;

public class TestAdminWebController {

    private static final int PORT = 8066;
    private GenericServer server;
    private AdminWebController admin = new AdminWebController(PORT);
    
    @Before
    public void startServer() throws Exception {
        server = new GenericServer();
        server.addTemplate(new TestGameTemplate("www\nwbw\nwww"));
    }
    
    @After
    public void stopServer() throws Exception {
        server.stop();
    }
    
    @Test
    public void addDigger() throws Exception {
    	admin.add("test1", "secret");

    	WebResponse resp = admin.listdiggers();
    	assertEquals("test1 secret", resp.getText().trim());
    	
    	admin.add("test2", "secret2");
    	admin.add("test1", "secret2");
    	
    	resp = admin.listdiggers();
    	assertEquals("test1 secret\ntest2 secret2", resp.getText().trim());
    }
}
    