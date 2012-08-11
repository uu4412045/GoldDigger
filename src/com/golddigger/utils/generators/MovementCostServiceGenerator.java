package com.golddigger.utils.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golddigger.server.GameService;
import com.golddigger.services.CarryingService;
import com.golddigger.services.DropService;
import com.golddigger.services.GrabService;
import com.golddigger.services.MoveService;
import com.golddigger.services.NextService;
import com.golddigger.services.ScoreService;
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
		services.add(new MoveService(costs));
		services.add(new NextService());
		services.add(new GrabService());
		services.add(new DropService());
		services.add(new ScoreService());
		services.add(new CarryingService());
		return services.toArray(new GameService[]{});
	}
}
