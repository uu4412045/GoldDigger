package com.golddigger.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.golddigger.model.Game;
import com.golddigger.model.Tile;
import com.golddigger.model.Coordinate;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.TeleportTile;
import com.golddigger.services.AdvTeleportService;
import com.golddigger.services.CannonService;
import com.golddigger.model.Coordinate;
import com.golddigger.services.DayNightService;
import com.golddigger.services.GameService;
import com.golddigger.services.GoldService;
import com.golddigger.services.HexMoveService;
import com.golddigger.services.OccludedHexViewService;
import com.golddigger.services.HexViewService;
import com.golddigger.services.MultiplayerService;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.OccludedViewService;
import com.golddigger.services.ViewService;
import com.golddigger.utils.ATeleportUtility;
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
	private boolean occlusion = false;
	private String[] dTeleportTiles; /* Disadvantageous */
	private DTeleportUtility dTeleportUtility;
	private long multiplayer_start, multiplayer_duration, multiplayer_end;
	private String[] advTeleportTiles = {};

	@Override
	public Game build() {
		Game game = new Game(getID());
		game.setMap(MapMaker.parse(map));
		game.add(new GoldService());
		
		if (numberOfSides == 6){
			game.add(new HexMoveService(formatCosts(costs)));
			if(occlusion) {
				game.add(new OccludedHexViewService(lineOfSight));
			} else {
				game.add(new HexViewService(lineOfSight));
			}
		} else {
			game.add(new SquareMoveService(formatCosts(costs)));
			if(occlusion) {
				game.add(new OccludedViewService(lineOfSight));
			} else {
				game.add(new ViewService(lineOfSight));
			}
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

		if (advTeleportTiles.length > 0){
			game.add(new AdvTeleportService());
			processAdvTeleportMappings(game.getMap());
		}
		
		return game;
	}

	public void setCosts(String[] costs){
		this.costs = costs;
	}

	public void setDayNight(int cycleTime, int scale){
		if (cycleTime > 0) this.cycleTime = cycleTime;
		if (scale > 0 ) this.scale = scale;
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
		} else if (name.equals(MultiplayerService.class.getName())) {
			return new MultiplayerService(multiplayer_start, multiplayer_duration, multiplayer_end);
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

	/**
	 * Enable Height based Occlusion using {@link Tile} height.
	 * @param occlude true to enable occlusion, false otherwise.
	 */
	public void enableOcclusion(boolean occlude){
		this.occlusion = occlude;
	}

	/**
	 * Set the timer durations for the multiplayer
	 * @param start the duration of the starting timer, -1 for default.
	 * @param duration the duration of the running timer, -1 for default. 
	 * @param end the duration of the ending timer, -1 for default.
	 */
	public void setMultiplayer(int start, int duration, int end) {
		this.multiplayer_start = start;
		this.multiplayer_duration = duration;
		this.multiplayer_end = end;
		for (String service : services){
			if (service.equals(MultiplayerService.class.getName())){
				return;
			}
		}

		services = Arrays.copyOf(services, services.length+1);
		services[services.length-1] = MultiplayerService.class.getName();
	}

	public void setATeleportTiles(String[] mappings) {
		if (mappings.length == 0) return;
		else {
			advTeleportTiles = mappings;
		}
	}

	public void processAdvTeleportMappings(com.golddigger.model.Map map){
		ATeleportUtility validator = new ATeleportUtility(map, numberOfSides == 6);
		for (String mapping : advTeleportTiles){
			String[] strs = mapping.split("->");
			Coordinate start = parsePoint2D(strs[0]);
			Coordinate end = parsePoint2D(strs[1]);
			if (start == null || end == null) {
				System.out.println("Bad Adv Teleport Mapping: "+mapping+" - start or end is malformed");
				continue;
			}

			if (!validator.isReachable(start) && !validator.isReachable(end)){
				System.out.println("Bad Adv Teleport Mapping: "+mapping+" - start and end both can not reach a base");
				continue;
			}

			Tile startTile = map.get(start);
			Tile endTile = map.get(end);
			if (startTile == null || endTile == null) {
				System.out.println("Bad Adv Teleport Mapping: "+mapping+" - start or end is out of bounds");
				continue;
			} else if (startTile instanceof TeleportTile || endTile instanceof TeleportTile){
				System.out.println("Bad Adv Teleport Mapping: "+mapping+" - start or end is on another teleport tile");
				continue;
			} else if (startTile instanceof BaseTile || endTile instanceof BaseTile){
				System.out.println("Bad Adv Teleport Mapping: "+mapping+" - start or end is on a base tile");
				continue;
			} else if (startTile instanceof GoldTile){
				if (((GoldTile) startTile).getGold() > 0) {
					System.out.println("Bad Adv Teleport Mapping: "+mapping+" - start is on a tile with 1 or more gold");
					continue;
				}
			} else if (endTile instanceof GoldTile){
				if (((GoldTile) endTile).getGold() > 0) {
					System.out.println("Bad Adv Teleport Mapping: "+mapping+" - end is on a tile with 1 or more gold");
					continue;
				}
			}

			TeleportTile teleStart = new TeleportTile(end, startTile);
			TeleportTile teleEnd = new TeleportTile(start, endTile);
			map.set(start.lat, start.lng, teleStart);
			map.set(end.lat, end.lng, teleEnd);
		}
	}

	private static Coordinate parsePoint2D(String string) {
		String[] coords = string.split(",");
		int lat = Integer.parseInt(coords[0].trim());
		int lng = Integer.parseInt(coords[1].trim());
		return new Coordinate(lat,lng);
	}
}
