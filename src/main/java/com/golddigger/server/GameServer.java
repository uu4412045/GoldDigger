package com.golddigger.server;

import java.util.ArrayList;
import java.util.List;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.services.ServerService;
import com.golddigger.templates.GameTemplate;

/**
 * This is the collection of games, templates and players on this server.
 * Its primary use is the allow each service access to the needed information.
 * 
 * @author Brett Wandel
 */
public class GameServer {

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

	/**
	 * All the services used to manage the server.
	 */
	private List<ServerService> services = new ArrayList<ServerService>();

	/** Gets the Game that the player is currently in.
	 * @param player The player
	 * @return <b>null<b> if they are not currently in a game.
	 * @see Game
	 * @see Player
	 */
	public Game getGame(Player player){
		for (Game game : this.games){
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
		this.games.add(game);
	}

	/**
	 * Add a Server Service to the server.
	 * @param service the service to add.
	 */
	public void add(ServerService service) {
		service.setServer(this);
		this.services.add(service);
	}
	
	/**
	 * Add a template to the server.
	 * This template will be used to build Games when a player completes the template added before it.
	 * @param template The template to be added.
	 * @see GameTemplate
	 */
	public void add(GameTemplate template){
		template.setID(templates.size());
		this.templates.add(template);
	}

	/**
	 * Add a player to the server. The will be immediately added to a {@link Game}, starting with the first {@link GameTemplate} in the servers queue
	 * @param player The player to add to the game
	 * @return False if the player has already been added the server
	 * @See GameTemplate
	 * @see Player
	 */
	public boolean add(Player player){
		if (exists(player)) return false;
		this.players.add(player);

		this.progress(player);
		return true;
	}

	/**
	 * Returns all the server services used in this server
	 */
	public ServerService[] getServices() {
		return this.services .toArray(new ServerService[]{});
	}
	
	/**
	 * Return the {@link Player} object with the this name;
	 * @param name The name of the player
	 * @return <b>null</b> if there is no player in the server with this name;
	 */
	public Player getPlayer(String name){
		for (Player player : this.players){
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
		// TODO: Add Tests for this
		// TODO: Make sure someone who has finished all the levels can't just start at the start again
		
		int id = -1;
		Game old = this.getGame(player);

		// Remove from old game (if in one)
		if (old != null){
			id = old.getTemplateID();
			if (old.remove(player) == 0) {
				this.games.remove(old);
			}
		}

		// Check to see if they still have levels to go
		if (this.templates.size() > id+1){
			// find any free multiplayer games
			for (Game game : this.games){
				if (game.getTemplateID() == id+1){
					if (game.hasUnownedBase()){
						if (game.add(player)) return;
					}
				}
			}
			// create a new game
			Game next = templates.get(id+1).build();
			this.add(next);
			next.add(player);
		} else {
			//player has finished
		}
	}


	/**
	 * Checks to see if this player currently exists in the server.
	 */
	public boolean exists(Player player){
		return this.players.contains(player);
	}

	/**
	 * Checks to see if this game currently exists in the server.
	 */
	public boolean exists(Game game){
		return this.games.contains(game);
	}

	/**
	 * Clear the server of data <br />
	 * <b>WARNING:</b> Deletes the server of all games, templates and players.<br />
	 * Should only be used when resetting or stopping the server.
	 */
	public void clear(){
		this.games = new ArrayList<Game>();
		this.templates = new ArrayList<GameTemplate>();
		this.players = new ArrayList<Player>();
	}

	/**
	 * return all the players in the server.
	 * @return
	 */
	public Player[] getPlayers() {
		return this.players.toArray(new Player[]{});
	}
}
