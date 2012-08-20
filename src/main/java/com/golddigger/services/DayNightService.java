package com.golddigger.services;

import java.io.PrintWriter;
import java.util.List;


/**
 * This service will imitate the a day/night effect by reducing the line of sight at night time. <br \>
 * It will scale the line of sight every "x" number of valid move commands issued.
 * @author Brett Wandel
 */
public class DayNightService extends GameService {
	private static final String ACTION_TEXT = "move";
	public static final int DEFAULT_CYCLE_TIME = 10;
	public static final int DEFAULT_SCALE = 50;

	/** Used to determine how many moves a player can make before the LOS is updated */
	private int cycleTime = DEFAULT_CYCLE_TIME;
	/** The current number of moves executed */
	private int current = 1;
	
	/** Determines how far to scale the LOS. should be in percentage.
	 * e.g. 50 would be 50% of the LOS at night time.
	 */
	private int scale= DEFAULT_SCALE;
	
	/**
	 * @param cycleTime The number of turns between each day/night switch
	 * @param scale The amount to scale line of sight by, in percentage.
	 */
	public DayNightService(int cycleTime, int scale) {
		super(BASE_PRIORITY+10);
		this.cycleTime = cycleTime;
		this.scale = scale;
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		
		List<ViewService> squareServices = game.getServices(ViewService.class);
		List<HexViewService> hexServices= game.getServices(HexViewService.class);
		
		boolean day = isDay();
		current++;
		if (day != isDay()){
			if (squareServices.size() > 0){
				ViewService squareService = squareServices.get(0);
				squareService.setLineOfSight(calc(squareService.getLineOfSight()));
			} else if (hexServices.size() > 0){
				HexViewService hexService = hexServices.get(0);
				hexService.setLineOfSight(calc(hexService.getLineOfSight()));
			} else {
				System.err.println("There is no view service for this game!");
			}
		}
		return false;
	}

	/**
	 * Calculate the new line of sight, uses isDay() to determine if it should scale up or down.
	 * @param currentLOS the current line of sight.
	 * @return The new LOS to be set.
	 */
	private int calc(int currentLOS){
		if (isDay()){
			currentLOS = (100/scale)*currentLOS;
		} else {
			currentLOS = (currentLOS*scale)/100;
		}
		return (int) Math.round(currentLOS);
	}
	
	/**
	 * @return <b>true</b> if its "day time", <b>false</b> otherwise.
	 */
	public boolean isDay() {
		if (cycleTime == 0) return true;
		int x = current/cycleTime;
		return (x % 2) == 0;
	}

	public int getCycleTime(){
		return this.cycleTime;
	}
	
	public int getScale(){
		return this.scale;
	}

}
