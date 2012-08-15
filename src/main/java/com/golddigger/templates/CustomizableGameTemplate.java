package com.golddigger.templates;

import java.util.HashMap;
import java.util.Map;

import com.golddigger.model.Game;
import com.golddigger.plugins.Plugin;
import com.golddigger.services.DayNightService;
import com.golddigger.services.GameService;
import com.golddigger.services.GoldService;
import com.golddigger.services.HexMoveService;
import com.golddigger.services.HexViewService;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.ViewService;
import com.golddigger.utils.MapMaker;

public class CustomizableGameTemplate extends GameTemplate {
	private String[] services = new String[]{};
	private String[] plugins = new String[]{};
	private String[] costs = new String[]{};
	private int lineOfSight = ViewService.DEFAULT_LINE_OF_SIGHT;
	private int cycleTime = DayNightService.DEFAULT_CYCLE_TIME, scale = DayNightService.DEFAULT_SCALE;
	private int numberOfSides = 4;
	private String map;

	@Override
	public Game build() {
		Game game = new Game(getID());
		game.setMap(MapMaker.parse(map));
		game.add(new GoldService());
		
		if (numberOfSides == 6){
			game.add(new HexMoveService(formatCosts(costs)));
			game.add(new HexViewService(lineOfSight));
		} else {
			game.add(new SquareMoveService(formatCosts(costs)));
			game.add(new ViewService(lineOfSight));
		}

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
	
	public void setNumberOfSides(int numberOfSides){
		this.numberOfSides = numberOfSides;
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
			String key = MapMaker.convert(s[0].charAt(0)).toString();
			Integer value = Integer.parseInt(s[1]);
			System.out.println("Customizable Game Template: Added Cost "+key+" => "+value);
			map.put(key, value);
		}
		return map;
	}

}
