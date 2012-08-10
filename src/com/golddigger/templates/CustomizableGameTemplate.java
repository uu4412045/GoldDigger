package com.golddigger.templates;

import java.util.HashMap;
import java.util.Map;

import com.golddigger.core.GameService;
import com.golddigger.core.GameTemplate;
import com.golddigger.model.Game;
import com.golddigger.plugins.Plugin;
import com.golddigger.services.CarryingService;
import com.golddigger.services.DayNightService;
import com.golddigger.services.DropService;
import com.golddigger.services.GrabService;
import com.golddigger.services.MoveService;
import com.golddigger.services.NextService;
import com.golddigger.services.ScoreService;
import com.golddigger.services.ViewService;
import com.golddigger.utils.MapMaker;

public class CustomizableGameTemplate extends GameTemplate {
	private String[] services;
	private String[] plugins;
	private String[] costs;
	private int lineOfSight = ViewService.DEFAULT_LINE_OF_SIGHT;
	private int cycleTime = DayNightService.DEFAULT_CYCLE_TIME, scale = DayNightService.DEFAULT_SCALE;
	private String map;

	@Override
	public Game build(String contextID) {
		Game game = new Game(getID(), contextID);
		game.setMap(MapMaker.parse(map));
		game.add(new ViewService(lineOfSight));
		if (costs != null) game.add(new MoveService(formatCosts(costs)));
		else game.add(new MoveService());
		game.add(new GrabService());
		game.add(new DropService());
		game.add(new CarryingService());
		game.add(new ScoreService());
		game.add(new NextService());

		if (services != null){
			for (String service : services){
				GameService s = buildService(service);
				if (s != null) game.add(s);
			}
		}

		if (plugins != null){
			for (String plugin : plugins){
				Plugin p = buildPlugin(plugin);
				if (p != null) game.add(p);
			}
		}
		return game;
	}

	public void setCosts(String[] costs){
		this.costs = costs;
	}

	public void setScale(int scale){
		this.scale = scale;
	}
	public void setCycleTime(int cycleTime){
		this.cycleTime = cycleTime;
	}
	public void setLineOfSight(int lineOfSight){
		this.lineOfSight = lineOfSight;
	}
	public void setServices(String[] services){
		this.services = services;
	}

	public void setPlugins(String[] plugins){
		this.plugins = plugins;
	}

	private GameService buildService(String name){
		if (name.equals(DayNightService.class.getName())) {
			return new DayNightService(cycleTime, scale);
		} else {
			return null;
		}
	}

	private Plugin buildPlugin(String name){
		return null;
	}

	public void setMap(String map){
		this.map = map;
	}

	private Map<String, Integer> formatCosts(String[] costs){
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String cost : costs){
			String[] s = cost.split("=");
			map.put(s[0], Integer.parseInt(s[1]));
		}
		return map;
	}

}
