package com.golddigger.templates;

import com.golddigger.model.Game;
/**
 * Game templates are used to create a new game.
 * A queue of templates is used to build an extended competition.
 * @author Brett Wandel
 */
public abstract class GameTemplate {
	private int gameID = -1;
	/**
	 * The position in the queue of templates.
	 * Used by the server to keep track of the which game a player is up to.
	 */
	public void setID(int id){this.gameID = id;}
	
	/**
	 * @return The position of this template in the queue
	 */
	public int getID(){return this.gameID;}
	
	/**
	 * Builds a new game for a player/players to be added to.
	 * @return The new game
	 */
	public abstract Game build();
}
