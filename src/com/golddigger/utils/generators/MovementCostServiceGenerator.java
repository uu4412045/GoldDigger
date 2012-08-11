package com.golddigger.utils.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golddigger.server.GameService;
import com.golddigger.services.GoldService;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.NextService;
import com.golddigger.services.ViewService;
import com.golddigger.services.old.CarryingService;
import com.golddigger.services.old.DropService;
import com.golddigger.services.old.GrabService;
import com.golddigger.services.old.ScoreService;

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
//		services.add(new GrabService());
//		services.add(new DropService());
//		services.add(new ScoreService());
//		services.add(new CarryingService());
		return services.toArray(new GameService[]{});
	}
}
