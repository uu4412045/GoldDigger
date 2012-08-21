package com.golddigger.utils;

import static org.junit.Assert.*;

import java.util.List;

import com.golddigger.services.Service;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.NextService;
import com.golddigger.services.ViewService;

public class ContainerTest {

	@org.junit.Test
	public void test() {
		Container<Service> container = new Container<Service>();
		container.add(new ViewService());
		container.add(new SquareMoveService());

		assertEquals(2,container.size());
		
		List<SquareMoveService> move = container.filter(SquareMoveService.class);
		assertEquals(1, move.size());
		assertTrue(move.get(0) instanceof SquareMoveService);
		
		List<NextService> next = container.filter(NextService.class);
		assertEquals(0, next.size());
		
		List<Service> all = container.filter(Service.class);
		assertEquals(2, all.size());
	}

}
