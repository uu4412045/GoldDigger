package com.golddigger.server;

import com.golddigger.model.Game;

/**
 * GameServices are what parse the urls and apply the logic to the game.
 * @author Brett Wandel
 *
 */
public abstract class GameService extends Service {
	protected Game game;
	
	public GameService(int priority) {
		super(priority);
	}
	
	/**
	 * Attach a game to this service
	 * @param game
	 */
	public void setGame(Game game){
		this.game = game;
	}
}
