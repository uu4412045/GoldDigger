package com.golddigger.templates;

import com.golddigger.core.GameTemplate;
import com.golddigger.model.BlankMap;
import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.services.DropService;
import com.golddigger.services.GrabService;
import com.golddigger.services.LogService;
import com.golddigger.services.MoveService;
import com.golddigger.services.NextService;
import com.golddigger.services.ViewService;

public class BlankGameTemplate implements GameTemplate {
	private int gameID = -1;
	private Map map = new BlankMap(10, 10);
	
	public BlankGameTemplate(int id){
		this.gameID = id;
	}
	
	@Override
	public Game build(){
		map.set(0, 0, new GoldTile(9));
		map.set(0, 1, new BaseTile());
		Game game = new Game(gameID);
		game.setMap(map);
		
		addPlugins(game);
		return game;
	}
	
	// Adding Default Plugins
	public void addPlugins(Game game){
		game.add(new LogService(gameID+""));
		game.add(new ViewService());
		game.add(new NextService());
		game.add(new MoveService());
		game.add(new GrabService());
		game.add(new DropService());
	}
}
