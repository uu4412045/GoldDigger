package com.golddigger.model;

import java.util.ArrayList;
import java.util.List;

import com.golddigger.core.AppContext;
import com.golddigger.core.Plugin;
import com.golddigger.core.Service;
import com.golddigger.model.tiles.BaseTile;

public class Game {
		private int id;
		private List<Service> services;
		private List<Plugin> plugins;
		private List<Player> players;
		private List<Unit> units;
		private Map map;

		public Game(int id){
			this.id = id;
			services = new ArrayList<Service>();
			players = new ArrayList<Player>();
			plugins = new ArrayList<Plugin>();
			units = new ArrayList<Unit>();
		}
		
		public int getID() {
			return this.id;
		}

		public void add(Service service){
			this.services.add(service);
		}
		
		public Service[] getServices(){
			return this.services.toArray(new Service[]{});
		}
		
		public void add(Plugin plugin){
			this.plugins.add(plugin);
		}
		
		public Plugin[] getPlugins(){
			return this.plugins.toArray(new Plugin[]{});
		}
		
		public boolean hasPlayer(Player player){
			return players.contains(player);
		}
		
		public void add(Player player){
			System.out.println("!! Adding New Player");
			if (AppContext.getGame(player) != null){
				System.out.println("!! Exceptions");
				throw new RuntimeException("Trying to add a player that already exists in another game");
			}
			this.players.add(player);

			System.out.println("!! Getting Next Start Location");
			Point2D start = getNextStartLocation();
			System.out.println("!! Start Location @ "+start.x+","+start.y);
			Unit digger = new Unit(player, start.x, start.y);
			units.add(digger);
		}
		
		private Point2D getNextStartLocation() {
			for (int x = 0; x <= map.getMaxX(); x++){
				for (int y = 0; y <= map.getMaxY(); y++){
					if (map.get(x, y) instanceof BaseTile) return new Point2D(x,y);
				}
			}
			return new Point2D(0,0);
		}

		public Map getMap(){
			return this.map;
		}
		
		public void setMap(Map map){
			this.map = map;
		}
		
		public Unit getUnit(Player player){
			for (Unit unit : units){
				if (unit.isOwnedBy(player)) return unit;
			}
			return null;
		}

		public void getUnitsAt(int i, int j) {
			// TODO Auto-generated method stub
			
		}
	}