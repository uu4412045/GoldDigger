package com.golddigger.services;

import java.io.PrintWriter;
import java.util.List;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;

public class DayNightService extends Service {
	private static final String ACTION_TEXT = "move";
	private static final int DEFAULT_NUMBER_OF_TURNS = 10;
	private int cycleTime = DEFAULT_NUMBER_OF_TURNS;
	private int current = 1;
	private int scale=50;
	
	public DayNightService(int numberOfTurns, int scale) {
		super(BASE_PRIORITY+10);
		this.cycleTime = numberOfTurns;
		this.scale = scale;
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		Service[] services = AppContext.getGame(AppContext.getPlayer(parseURL(url, URL_PLAYER))).getServices();
		ViewService vService = null;
		for (Service service : services){
			if (service instanceof ViewService) vService = (ViewService) service;
		}
		
		if (vService == null){
			System.err.println("ERROR: No Movement Service Found, Yet DayNight Service is Active");
			return false;
		}
		
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

	public boolean isDay() {
		if (cycleTime == -1) return true;
		int x = current/cycleTime;
		return (x % 2) == 0;
	}

}
