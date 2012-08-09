package com.golddigger.utils.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golddigger.core.GameService;
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
	public GameService[] generate() {
		List<GameService> services = new ArrayList<GameService>();
		services.add(new ViewService(los));
		if (costs == null){
			services.add(new MoveService());
		}else {
			services.add(new MoveService( new HashMap<String, Integer>(costs)));
		}
		services.add(new NextService());
		services.add(new GrabService());
		services.add(new DropService());
		services.add(new ScoreService());
		services.add(new CarryingService());
		return services.toArray(new GameService[]{});
	}
	
	public void setLOS(int los){
		this.los = los;
	}
	
	public void setCosts(Map<String, Integer> costs){
		this.costs = costs;
	}

}
