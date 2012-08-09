package com.golddigger.services;

import java.io.PrintWriter;
import java.util.List;

import com.golddigger.core.GameService;
import com.golddigger.core.Service;

/**
 * This service will imitate the a day/night effect by reducing the line of sight at night time. <br \>
 * It will scale the line of sight every "x" number of valid move commands issued.
 * @author Brett Wandel
 */
public class DayNightService extends GameService {
	private static final String ACTION_TEXT = "move";
	private static final int DEFAULT_NUMBER_OF_TURNS = 10;
	private int cycleTime = DEFAULT_NUMBER_OF_TURNS;
	private int current = 1;
	private int scale=50;
	
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
		List<ViewService> vServices = game.getServices(ViewService.class);
		if (vServices.size() < 1){
			System.err.println("DayNightPlugin No View Service Found: "+url);
			return false;
		}
		ViewService vService = vServices.get(0);
		
		boolean day = isDay();
		current++;
		if (day != isDay()){
			double newLOS= vService.getLineOfSight();
			if (isDay()){
				newLOS = (100/scale)*newLOS;
			} else {
				newLOS = (newLOS*scale)/100;
			}
			vService.setLineOfSight((int) Math.round(newLOS));
		}
		return false;
	}

	/**
	 * @return <b>true</b> if its "day time", <b>false</b> otherwise.
	 */
	public boolean isDay() {
		if (cycleTime == 0) return true;
		int x = current/cycleTime;
		return (x % 2) == 0;
	}

}
