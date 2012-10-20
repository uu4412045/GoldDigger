package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.templates.TestGameTemplate;

public class AdminServiceTest {
	GenericServer server;
	TestingClient client; 
	
	@Before
	public void before(){
		server = new GenericServer();
		client = new TestingClient("secret1", "http://localhost:8066/");
		server.addTemplate(new TestGameTemplate("www\nwbw\nwww"));
	}

	@After
	public void after(){
		server.stop();
	}
	
	@Test
	public void test() {
		
//		Testing for correct defaut output
		String res = client.doGET("http://localhost:8066/golddigger/admin/ccret/listdiggers");
		assertEquals("", res.trim());
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test1/secret1");
		assertEquals("OK", res.trim());
		
//		Making sure invalid commands faile
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test1/secret1");
		assertEquals("FAILED", res.trim());
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test1/");
		assertEquals("FAILED", res.trim());
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/");
		assertEquals("FAILED", res.trim());
		
//		making sure the new player is added to listdiggers
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/listdiggers");
		assertEquals("test1 secret1", res.trim());
		
//		testing adding a second player doesn't affect the output
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test2/secret2");
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/listdiggers");
		assertEquals("test1 secret1\ntest2 secret2", res.trim());
		
	}

}
