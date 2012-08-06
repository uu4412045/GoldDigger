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

public class BaseServiceGenerator implements ServiceGenerator {
	private int los = 1;
	private Map<String, Integer> costs = new HashMap<String, Integer>();
	@Override
	public Service[] generate(String contextId) {
		List<Service> services = new ArrayList<Service>();
		services.add(new ViewService(contextId,los));
		if (costs == null){
			services.add(new MoveService(contextId));
		}else {
			services.add(new MoveService(contextId, new HashMap<String, Integer>(costs)));
		}
		services.add(new NextService(contextId));
		services.add(new GrabService(contextId));
		services.add(new DropService(contextId));
		services.add(new ScoreService(contextId));
		services.add(new CarryingService(contextId));
		return services.toArray(new Service[]{});
	}
	
	public void setLOS(int los){
		this.los = los;
	}
	
	public void setCosts(Map<String, Integer> costs){
		this.costs = costs;
	}

}
