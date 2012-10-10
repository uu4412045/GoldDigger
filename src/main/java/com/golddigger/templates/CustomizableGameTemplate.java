package com.golddigger.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.golddigger.model.Game;
import com.golddigger.services.CannonService;
import com.golddigger.model.Coordinate;
import com.golddigger.services.DayNightService;
import com.golddigger.services.GameService;
import com.golddigger.services.GoldService;
import com.golddigger.services.HexMoveService;
import com.golddigger.services.HexViewService;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.ViewService;
import com.golddigger.utils.DTeleportUtility;
import com.golddigger.utils.MapMaker;

public class CustomizableGameTemplate extends GameTemplate {
	private String[] services = new String[]{};
	private String[] costs = new String[]{};
	private int lineOfSight = ViewService.DEFAULT_LINE_OF_SIGHT;
	private int cycleTime = DayNightService.DEFAULT_CYCLE_TIME, scale = DayNightService.DEFAULT_SCALE;
	private int numberOfSides = 4;
	private String map;
	private boolean cannonsEnabled = false;
	private String[] dTeleportTiles; /* Disadvantageous */
	private DTeleportUtility dTeleportUtility;

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
		
		if(cannonsEnabled) {
			game.add(new CannonService());
		}

		if (services != null){
			for (String service : services){
				GameService s = buildService(service);
				if (s != null) game.add(s);
			}
		}
		
		if (dTeleportTiles != null) {
			dTeleportUtility = new DTeleportUtility();

			ArrayList<Coordinate[]> dTeleportPairs = dTeleportUtility.formatDTeleports(dTeleportTiles);

			dTeleportPairs = dTeleportUtility.validDTeleports(dTeleportPairs,game.getMap(), numberOfSides);

			dTeleportUtility.assignDTeleports(dTeleportPairs, game.getMap());
		}
		return game;
	}

	public void setCosts(String[] costs){
		this.costs = costs;
	}

	public void setDayNight(int cycleTime, int scale){
		this.cycleTime = cycleTime;
		this.scale = scale;
		for (String service : services){
			if (service.equals(DayNightService.class.getName())){
				return;
			}
		}
		
		services = Arrays.copyOf(services, services.length+1);
		services[services.length-1] = DayNightService.class.getName();
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

	private GameService buildService(String name){
		if (name.equals(DayNightService.class.getName())) {
			return new DayNightService(cycleTime, scale);
		} else {
			return null;
		}
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
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Allow the use of cannons.
	 * @param enable true if you want units to be equipped with cannons.
	 */
	public void enableCannons(boolean enabled) {
			this.cannonsEnabled  = enabled;
	}

	/** Set the disadventageous teleport coordinates.
	 *  each pair should be specified by "lat,lng - lat,lng"
	 *  where the first coordinate is the source, and the second is the
	 *  destination
	 *  @param array of teleport coordinate pairs.
	 */
	public void setDTeleportTiles(String[] dTeleportTiles) {
		this.dTeleportTiles = dTeleportTiles;
	}
}
