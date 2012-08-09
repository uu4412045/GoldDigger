package com.golddigger.utils;

import static org.junit.Assert.*;

import java.util.List;

import com.golddigger.core.Service;
import com.golddigger.plugins.Plugin;
import com.golddigger.services.MoveService;
import com.golddigger.services.NextService;
import com.golddigger.services.ViewService;

public class ContainerTest {

	@org.junit.Test
	public void test() {
		Container<Service> container = new Container<Service>();
		container.add(new ViewService());
		container.add(new MoveService());

		assertEquals(2,container.size());
		
		List<MoveService> move = container.filter(MoveService.class);
		assertEquals(1, move.size());
		assertTrue(move.get(0) instanceof MoveService);
		
		List<NextService> next = container.filter(NextService.class);
		assertEquals(0, next.size());
		
		List<Service> all = container.filter(Service.class);
		assertEquals(2, all.size());
	}

}
