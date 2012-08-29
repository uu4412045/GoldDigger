package com.golddigger.services;

import java.io.PrintWriter;
import java.util.List;

import com.golddigger.model.Direction;


/**
 * This service will imitate the a day/night effect by reducing the line of sight at night time. <br \>
 * It will scale the line of sight every "x" number of valid move commands issued.
 * @author Brett Wandel
 */
/*TODO: need to clean this service up. it should probably be initalised with as either hex or square.
 * Brett Wandel - 22/8/2012
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
		boolean isHex = (hexServices.size() == 1 && squareServices.size() == 0);

		Direction direction = Direction.parse(parseURL(url, URL_EXTRA1));
		if (direction == null || isInvalidMove(isHex, direction)) {
			return false; //invalid direction, shouldn't increase the turn count
		}

		boolean day = isDay();
		current++;
		if (day != isDay()){
			if (isHex){
				HexViewService hexService = hexServices.get(0);
				hexService.setLineOfSight(calc(hexService.getLineOfSight()));
			} else {
				ViewService squareService = squareServices.get(0);
				squareService.setLineOfSight(calc(squareService.getLineOfSight()));
			}
		}
		return false;
	}

	/**
	 * Determine if the Direction is a valid move direction for the number of sides
	 * @param isHex is the ViewService a HexViewService
	 * @param moveDirection The Direction that the unit is moving in.
	 * @return true if it is a valid move
	 */
	private boolean isInvalidMove(boolean isHex, Direction moveDirection){
		if (isHex){
			if (moveDirection == Direction.EAST) return true;
			if (moveDirection == Direction.WEST) return true;
		} else {
			if (moveDirection == Direction.SOUTH_EAST) return true;
			if (moveDirection == Direction.SOUTH_WEST) return true;
			if (moveDirection == Direction.NORTH_EAST) return true;
			if (moveDirection == Direction.NORTH_WEST) return true;
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
	 * The number of "turns" or valid move commands executed so far.
	 * @return
	 */
	public int getTurnCount(){
		return this.current;
	}
	
	/**
	 * @return <b>true</b> if its "day time", <b>false</b> otherwise.
	 */
	public boolean isDay() {
		if (cycleTime == 0) return true;
		int x = current/cycleTime;
		return (x % 2) == 0;
	}

	/**
	 * The CycleTime is the number of turns it takes to go from day to night and vise versa.
	 * @return The cycleTime
	 */
	public int getCycleTime(){
		return this.cycleTime;
	}
	
	/**
	 * The scale is the percentage that the Line of Sight is scaled by at night time.
	 * e.g: 50 = half line of sight at night time. 25 is a quarter.
	 * @return
	 */
	public int getScale(){
		return this.scale;
	}

}
