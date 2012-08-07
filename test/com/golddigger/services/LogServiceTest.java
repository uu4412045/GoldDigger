package com.golddigger.services;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.golddigger.client.TestingClient;
import com.golddigger.core.Service;
import com.golddigger.core.ServiceGenerator;
import com.golddigger.model.Player;
import com.golddigger.server.TestServer;
import com.golddigger.templates.TestGameTemplate;

public class LogServiceTest {
	TestServer server;
	TestingClient client;
	private static final String MAP = "wwwwwwwwwwwwww\nwbbcdshfrmt.19w\nwwwwwwwwwwwww";
	private static final String BASE_URL = "http://localhost:8066";

	@Before()
	public void setup(){
		server = new TestServer();
		ServiceGenerator gen = new ServiceGenerator(){
			@Override
			public Service[] generate(String contextID) {
				return new Service[]{new MoveService(contextID), new ViewService(contextID), new LogService(contextID)};
			}
		};
		server.getContext().add(new TestGameTemplate(MAP, gen));
		server.getContext().add(new Player("test", "secret"));
		client = new TestingClient("test", BASE_URL);
	}
	
	@After
	public void stop(){
		server.stop();
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
