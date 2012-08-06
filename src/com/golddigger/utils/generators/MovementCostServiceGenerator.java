package com.golddigger.utils.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golddigger.core.Service;
import com.golddigger.core.ServiceGenerator;
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
	public Service[] generate(String contextID) {
		List<Service> services = new ArrayList<Service>();
		services.add(new ViewService(contextID));
		services.add(new MoveService(contextID, costs));
		services.add(new NextService(contextID));
		services.add(new GrabService(contextID));
		services.add(new DropService(contextID));
		services.add(new ScoreService(contextID));
		services.add(new CarryingService(contextID));
		return services.toArray(new Service[]{});
	}
}
