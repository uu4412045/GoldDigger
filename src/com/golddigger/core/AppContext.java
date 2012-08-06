package com.golddigger.core;

import java.util.ArrayList;
import java.util.List;

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
public class AppContext {
	/**
	 * All the "games" currently running in the server
	 * @see Game
	 */
	public static List<Game> games = new ArrayList<Game>();
	
	/**
	 * A queue of all the templates to be played.
	 * @see GameTemplate
	 */
	public static List<GameTemplate> templates = new ArrayList<GameTemplate>();
	
	/**
	 * All the people that are currently playing.
	 * @see Player
	 */
	public static List<Player> players = new ArrayList<Player>();
	
	/** Gets the Game that the player is currently in.
	 * @param player The player
	 * @return <b>null<b> if they are not currently in a game.
	 * @see Game
	 * @see Player
	 */
	public static Game getGame(Player player){
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
	private static void add(Game game) {
		AppContext.games.add(game);
	}
	
	/**
	 * Add a template to the server.
	 * This template will be used to build Games when a player completes the template added before it.
	 * @param template The template to be added.
	 * @see GameTemplate
	 */
	public static void add(GameTemplate template){
		template.setID(templates.size());
		AppContext.templates.add(template);
	}
	
	/**
	 * Add a player to the server. The will be immediately added to a {@link Game}, starting with the first {@link GameTemplate} in the servers queue
	 * @param player
	 * @See GameTemplate
	 * @see Player
	 */
	public static void add(Player player){
		if (exists(player)) throw new RuntimeException("The Player ("+player.getName()+") is already playing");
		AppContext.players.add(player);
		Game first = templates.get(0).build();
		add(first);
		first.add(player);
	}
	
	/**
	 * Return the {@link Player} object with the this name;
	 * @param name The name of the player
	 * @return <b>null</b> if there is no player in the server with this name;
	 */
	public static Player getPlayer(String name){
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
	public static void progress(Player player){
		Game old = getGame(player);
		int id = old.getTemplateID();
		games.remove(old);
		Game next = templates.get(id+1).build();
		add(next);
		next.add(player);
	}
	
	/**
	 * Checks to see if this player currently exists in the server.
	 * @param player The player to check for
	 * @return <b>true</b> if they do, <b>false</b> if they dont
	 */
	private static boolean exists(Player player){
		return AppContext.getPlayer(player.getName()) != null;
	}
	
	/**
	 * <b>WARNING:</b> Clears the server of all games, templates and players.<br />
	 * Should only be used when resetting or stopping the server.
	 */
	public static void clear(){
		games = new ArrayList<Game>();
		templates = new ArrayList<GameTemplate>();
		players = new ArrayList<Player>();
	}
}
		