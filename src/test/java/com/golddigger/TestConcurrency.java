package com.golddigger;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.golddigger.model.Player;
import com.golddigger.templates.TestGameTemplate;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;


public class TestConcurrency {
    private ServletServer server;
	
    @Before
    public void startServer() throws Exception {
    	server = new ServletServer();
    }
    
    @After
    public void stopServer() throws Exception {
        server.stop();
    }
    @Test
    public void testMove() throws Exception {   
    	server.add(new TestGameTemplate("wwwww\nwmbmw\nwwwww"));
    	server.add(new Player("test", "secret"));
        Worker worker1 = new Worker("http://localhost:8066/golddigger/digger/secret/move/east");
        Worker worker2 = new Worker("http://localhost:8066/golddigger/digger/secret/move/west");
        Thread t1 = new Thread(worker1);
        Thread t2 = new Thread(worker2);

        t1.start();
		Thread.sleep(200);
        t2.start();
        
        while (t1.isAlive() || t2.isAlive()) Thread.sleep(100);
        assertEquals("Sanity Check Failed",200, worker1.response.getResponseCode());
        assertEquals("Did not error",503, worker2.response.getResponseCode());
        
    }
    
    private class Worker implements Runnable {
    	String URL;
    	public WebResponse response;
    	public Worker(String URL){
    		this.URL = URL;
    	}
		@Override
		public void run() {
	        WebConversation wc = new WebConversation();
	        wc.setExceptionsThrownOnErrorStatus(false);
	        try {
				response = wc.getResponse(URL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
	}
}