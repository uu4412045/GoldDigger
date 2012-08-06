package com.golddigger.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.golddigger.core.AppContext;
import com.golddigger.core.Plugin;
import com.golddigger.core.Service;
import com.golddigger.model.tiles.BaseTile;

/**
 * This is the object that contains all the needed information about a game.
 * 
 * <p>Each game contains its own services and plugins, which allow the services and plugins
 * to customized for each game. These services and plugins are the actual objects that modify
 * the data in the game, and this class is simply a container for all the data needed. They are
 * what actually contain the "business logic" behind how a game is run.</p>
 * 
 * @author Brett Wandel
 * @see Service
 */
public class Game {
	/**
	 * All the services that this game uses.
	 * @see Service
	 */
		private List<Service> services;
		
		/**
		 * All the plugins that this game uses.
		 * @See Plugin
		 */
		private List<Plugin> plugins;
		
		/**
		 * All the players in this game
		 * @see Player
		 */
		private List<Player> players;
		
		/**
		 * All the units in this game
		 * @see Unit
		 */
		private List<Unit> units;
		
		/**
		 * The "field" or map of this game
		 * @see Map
		 */
		private Map map;

		/**
		 * Used to determine what game or "level" the player is currently up to.
		 */
		private int templateID;
		
		public Game(int templateID){
			this.templateID = templateID;
			services = new ArrayList<Service>();
			players = new ArrayList<Player>();
			plugins = new ArrayList<Plugin>();
			units = new ArrayList<Unit>();
		}

		/**
		 * The if of the {@link GameTemplate} this game was generated from.
		 * @return The position of the GameTemplate in the server's template queue
		 */
		public int getTemplateID(){
			return this.templateID;
		}
		
		/**
		 * Add a {@link Service} to the game
		 * @param service The service to be added
		 * @see Service
		 */
		public void add(Service service){
			this.services.add(service);
			Collections.sort(services);
		}
		
		/**
		 * @return the services used by this game.
		 * @see Service
		 */
		public Service[] getServices(){
			return this.services.toArray(new Service[]{});
		}
		
		/**
		 * Add a {@link Plugin} to the game
		 * @param plugin The plugin to be added
		 * @see Plugin
		 */
		public void add(Plugin plugin){
			this.plugins.add(plugin);
		}
		
		/**
		 * @return All the plugins used by this game
		 * @see Plugin
		 */
		public Plugin[] getPlugins(){
			return this.plugins.toArray(new Plugin[]{});
		}
		
		/**
		 * Check to see if a {@link Player} is currently in this game.
		 * @param player The Player to check for
		 * @return <b>true</b> if they are, <false> if they are not.
		 */
		public boolean hasPlayer(Player player){
			return players.contains(player);
		}
		
		/**
		 * Add a {@link Player} to the game. checks to make sure the player isn't already in the game.
		 * @param player The player to add.
		 * @see Player
		 */
		public void add(Player player){
			Point2D start = getBasePosition();
			if (start == null) add(player, 1,1);
			else add(player, start.x, start.y);
		}
		
		/**
		 * Add a {@link Player} to the game. checks to make sure the player isn't already in the game.
		 * Places their first unit at the given location
		 * @param player The player to add.
		 * @param startX The x coordinate of their unit's starting location
		 * @param startY The y coordinate of their unit's starting location
		 * @see Player
		 */
		public void add(Player player, int startX, int startY){
			if (AppContext.getGame(player) != null){
				throw new RuntimeException("Trying to add a player that already exists in another game");
			}
			this.players.add(player);
			Unit digger = new Unit(player, startX, startY);
			units.add(digger);
		}
		
		/**
		 * Returns the location of the first base
		 * @return <b>null</b> if there is no bases.
		 */
		private Point2D getBasePosition() {
			for (int x = 0; x <= map.getMaxX(); x++){
				for (int y = 0; y <= map.getMaxY(); y++){
					if (map.get(x, y) instanceof BaseTile) return new Point2D(x,y);
				}
			}
			return null;
		}

		public Map getMap(){
			return this.map;
		}
		
		public void setMap(Map map){
			this.map = map;
		}
		
		/**
		 * gets the unit for a particular player
		 * @param player The player who owns the unit.
		 * @return <b>null</b> if there is no unit or player in this game.
		 */
		public Unit getUnit(Player player){
			if (player == null || !players.contains(player)) return null;
			for (Unit unit : units){
				if (unit.isOwnedBy(player)) return unit;
			}
			return null;
		}
	}