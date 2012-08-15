package com.golddigger.services.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golddigger.services.GameService;
import com.golddigger.services.GoldService;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.NextService;
import com.golddigger.services.ViewService;

public class MovementCostServiceGenerator implements ServiceGenerator{
	Map<String, Integer> costs;
	public MovementCostServiceGenerator(Map<String, Integer> costs){
		this.costs = new HashMap<String, Integer>(costs);
	}

	@Override
	public GameService[] generate() {
		List<GameService> services = new ArrayList<GameService>();
		services.add(new ViewService());
		services.add(new SquareMoveService(costs));
		services.add(new GoldService());
		return services.toArray(new GameService[]{});
	}
}
