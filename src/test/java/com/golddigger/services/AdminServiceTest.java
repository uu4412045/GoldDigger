package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.Test;

import com.golddigger.GenericServer;
import com.golddigger.client.TestingClient;
import com.golddigger.templates.TestGameTemplate;

public class AdminServiceTest {

	@Test
	public void test() {
		GenericServer server = new GenericServer();
		TestingClient client = new TestingClient("test1", "http://localhost:8066/"); 
		server.addTemplate(new TestGameTemplate("www\nwbw\nwww"));
		
//		Testing for correct defaut output
		String res = client.doGET("http://localhost:8066/golddigger/admin/ccret/listdiggers");
		assertEquals("", res.trim());
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test1/secret");
		assertEquals("OK", res.trim());
		
//		Making sure invalid commands faile
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test1/secret");
		assertEquals("FAILED", res.trim());
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test1/");
		assertEquals("FAILED", res.trim());
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/");
		assertEquals("FAILED", res.trim());
		
//		making sure the new player is added to listdiggers
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/listdiggers");
		assertEquals("test1 secret", res.trim());
		
//		testing adding a second player doesn't affect the output
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/add/test2/secret");
		res = client.doGET("http://localhost:8066/golddigger/admin/ccret/listdiggers");
		assertEquals("test1 secret\ntest2 secret", res.trim());
	}

}
