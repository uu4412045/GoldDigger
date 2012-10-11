package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.model.Coordinate;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
/**
 * <p>Multiplayer Service filters commands based on the 4 "game states" for a multiplayer game:
 * <ol>
 * <li>Starting - no commands are accepted (gives players time to join the game)</li>
 * <li>Running - no more players can join, all commands are executed as normal</li>
 * <li>Ending - all gold is removed, players should only be banking the gold they are currently holding.</li>
 * <li>Finished - no commands are accepted (other than "next"), game is over</li>
 * </ol></p>
 *
 * <p><b>IMPORTANT:</b> This service should have a higher priority than any service that should
 * be affected by the time restrictions.</p>
 * <p>NOTES: <br />
 * All this service does is imposes the timing and command restrictions for multiplayer, as well
 * as provide the current state information at the beginning of a view command.
 * <br />
 * The actual multiplayer logic (such as unit collisions) is handled by the model ({@link Game},
 * {@link GameServer}, etc) and other services such as the {@link MoveService}.</p>
 * 
 * @author Brett Wandel
 */
/*
 * At the moment actual commands are not checked during the "Ending" stage.
 * All the gold is removed from the map when the stage changes from Running -> Ending.
 * This will allow users to drop the gold on the map and possibly stop others from progressing.
 * If this is deemed a problem, you should be able to check the "url" to see if its a drop command
 * and filter on that.
 * 
 * Similarly, we stop players joining after the "Starting" stage by replacing any unowned bases
 * with empty gold tiles. I don't foresee any issues with this, but again we are not filtering commands.
 * 
 * Brett Wandel - 11/9/2012
 */
public class MultiplayerService extends GameService {
	private static final int MINUTES = 60000;
	
	/**
	 * Any {@link GameService} that should be affected by the multiplayer service should have a
	 * priority between {@link Service}'s BASE_PRIORITY and this value.
	 */
	private final static int PRIORITY = 1000;

	private static final long DEFAULT_STARTING_TIME = 1*MINUTES;
	private static final long DEFAULT_RUNNING_TIME = 10*MINUTES;
	private static final long DEFAULT_ENDING_TIME = 2*MINUTES;
	
	/**
	 * The duration (in milliseconds) between the c'tor call and when units are allowed to execute commands.
	 */
	private long startingEndTime;
	
	/**
	 * The duration (in milliseconds) that players are allowed to execute all commands.
	 */
	private long runningEndTime;
	
	/**
	 * The duration (in milliseconds) that the players are allowed to deposit
	 * their gold (held by units) before all commands are stopped.
	 */
	private long endingEndTime;
	
	/**
	 * The current system time when the multiplayer service started (c'tor was called)
	 */
	private long started;
	
	/**
	 * The current state of the game.
	 */
	private State currentState = State.STARTING;

	/** Setting up the timing durations for each stage. Use a value of -1 for the default time.
	 * 
	 * @param joining The duration (in milliseconds) that the STARTING state runs for.
	 * @param duration The duration (in milliseconds) that the RUNNING state runs for. 
	 * @param end The maximum duration (in milliseconds) that the ENDING state runs.
	 */
	public MultiplayerService(long joining, long duration, long end) {
		super(BASE_PRIORITY + PRIORITY); //Want to override almost all game services
		this.started = System.currentTimeMillis();
		this.startingEndTime = (joining == -1 ? DEFAULT_STARTING_TIME : joining);
		this.runningEndTime = this.startingEndTime+(duration == -1 ? DEFAULT_RUNNING_TIME : duration);
		this.endingEndTime = this.runningEndTime+(end == -1 ? DEFAULT_ENDING_TIME : end);
	}

	@Override
	public boolean runnable(String url) {
		String name = parseURL(url, URL_PLAYER);
		Player player = game.getPlayer(name);
		return player != null;
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		updateCurrentStatus();
		// output the extra status when view is called.
		String action = parseURL(url, URL_ACTION);
		if (action != null && action.equalsIgnoreCase("view")){
			out.println("state: "+getState()+","+getTimeLeft());
		}
		
		if (currentState == State.STARTING){
			out.println("FAILED: Joining Only");
			return true;
		} else if (currentState == State.FINISHED) {
			out.println("FAILED: Game Over");
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check the currentState of the game and update if needed.
	 */
	private void updateCurrentStatus(){
		long elapsed = System.currentTimeMillis() - this.started;
		switch (this.currentState){
		case STARTING:
			if (elapsed > startingEndTime) {
				this.currentState = State.RUNNING;
				removedUnownedBases();
			} else {
				break;
			}
		case RUNNING:
			if (elapsed > runningEndTime || !game.getMap().hasGoldLeft()) {
				this.currentState = State.ENDING;
				removeGoldFromField();
			} else {
				break;
			}
		case ENDING:
			if (elapsed > endingEndTime){
				this.currentState = State.FINISHED;
				removeGoldFromField();
				removeGoldFromUnits();
			} else {
				break;
			}
		default: break;
		}
	}

	/**
	 * Removes the empty bases from the game.
	 * Used to stop more players from joining the game.
	 */
	private void removedUnownedBases(){
		for (BaseTile base : game.getBases()){
			if (base.getOwner() == null){
				Coordinate pos = game.getMap().getPosition(base);
				game.getMap().set(pos.lat, pos.lng, new GoldTile());
			}
		}
	}

	/**
	 * Removes all the gold from the ground by replacing gold tiles with empty tiles.
	 */
	private void removeGoldFromField(){
		for (Tile[] row : game.getMap().getTiles()){
			for (Tile tile : row){
				if (tile instanceof GoldTile){
					((GoldTile) tile).setGold(0);
				}
			}
		}
	}

	/**
	 * Removes all the gold from every unit in the game.
	 */
	private void removeGoldFromUnits(){
		for (Unit unit : game.getUnits()){
			unit.setGold(0);
		}
	}

	private enum State {
		STARTING, RUNNING, ENDING, FINISHED;
	}

	/** Used for multiplayer view output
	 * @return The current state of this game.
	 */
	private String getState() {
		return currentState.toString().toLowerCase();
	}
	
	/** Used for multiplayer view output
	 * @return How much time is left of the current state in milliseconds
	 */
	private long getTimeLeft(){
		long elapsed = System.currentTimeMillis() - started;
		switch( this.currentState){
		case STARTING: return startingEndTime - elapsed;
		case RUNNING: return runningEndTime - elapsed;
		case ENDING: return endingEndTime - elapsed;
		default: return 0;
		}
	}

}
