package com.golddigger.core.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.golddigger.core.GameTemplate;
import com.golddigger.model.Game;
import com.golddigger.model.Player;

/**
 * This is the collection of games, templates and players on this server.
 * Its primary use is the allow each service access to the needed information.
 * 
 * @author Brett Wandel
 */
/*
 * I want to re-factor all of this so that each instance of a server has its own "context",
 * but I haven't found the time just yet.
 */
public abstract class GameServer {
	
	
	/**
	 * All the "games" currently running in the server
	 * @see Game
	 */
	private List<Game> games = new ArrayList<Game>();
	
	/**
	 * A queue of all the templates to be played.
	 * @see GameTemplate
	 */
	private List<GameTemplate> templates = new ArrayList<GameTemplate>();
	
	/**
	 * All the people that are currently playing.
	 * @see Player
	 */
	private List<Player> players = new ArrayList<Player>();

	/** Gets the Game that the player is currently in.
	 * @param player The player
	 * @return <b>null<b> if they are not currently in a game.
	 * @see Game
	 * @see Player
	 */
	public Game getGame(Player player){
		for (Game game:games){
			if (game.hasPlayer(player)){
				return game;
			}
		}
		return null;
	}

	/**
	 * Add a game to the server. All games should have been build using a template in the "templates" list
	 * @param game The game to add.
	 * @see Game
	 */
	private void add(Game game) {
		games.add(game);
	}
	
	/**
	 * Add a template to the server.
	 * This template will be used to build Games when a player completes the template added before it.
	 * @param template The template to be added.
	 * @see GameTemplate
	 */
	public void add(GameTemplate template){
		template.setID(templates.size());
		templates.add(template);
	}
	
	/**
	 * Add a player to the server. The will be immediately added to a {@link Game}, starting with the first {@link GameTemplate} in the servers queue
	 * @param player
	 * @See GameTemplate
	 * @see Player
	 */
	public void add(Player player){
		if (exists(player)) throw new RuntimeException("The Player ("+player.getName()+") is already playing");
		players.add(player);
		
		//TODO: Make sure multiplayer game has templateid of 0.
		//Check Multiplayer
		for(Game game : games){
			if (game.hasUnownedBase()) {
				game.add(player);
				return;
			}
		}
		//create new game
		Game first = templates.get(0).build();
		add(first);
		first.add(player);
	}
	
	/**
	 * Return the {@link Player} object with the this name;
	 * @param name The name of the player
	 * @return <b>null</b> if there is no player in the server with this name;
	 */
	public Player getPlayer(String name){
		for (Player player : players){
			if (player.getName().equalsIgnoreCase(name)){
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Moves this player on to the next game.
	 * @param player The player to move on
	 * @see Player
	 */
	public void progress(Player player){
		Game old = getGame(player);
		int id = old.getTemplateID();
		if (old.remove(player) == 0) games.remove(old);

		// find any multiplayer games.
		for (Game game : games){
			if (game.getTemplateID() == id+1){
				if (game.hasUnownedBase()){
					if (game.add(player)) return;
				}
			}
		}
		// create a new game
		Game next = templates.get(id+1).build();
		add(next);
		next.add(player);
	}
	
	/**
	 * Checks to see if this player currently exists in the server.
	 * @param player The player to check for
	 * @return <b>true</b> if they do, <b>false</b> if they dont
	 */
	private boolean exists(Player player){
		return getPlayer(player.getName()) != null;
	}
	
	/**
	 * <b>WARNING:</b> Clears the server of all games, templates and players.<br />
	 * Should only be used when resetting or stopping the server.
	 */
	public void clear(){
		games = new ArrayList<Game>();
		templates = new ArrayList<GameTemplate>();
		players = new ArrayList<Player>();
	}
}
		