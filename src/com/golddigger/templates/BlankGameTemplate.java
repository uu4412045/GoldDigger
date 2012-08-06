package com.golddigger.templates;

import com.golddigger.core.GameTemplate;
import com.golddigger.model.BlankMap;
import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.services.CarryingService;
import com.golddigger.services.DropService;
import com.golddigger.services.GrabService;
import com.golddigger.services.MoveService;
import com.golddigger.services.NextService;
import com.golddigger.services.ScoreService;
import com.golddigger.services.ViewService;

public class BlankGameTemplate extends GameTemplate {
	private Map map = new BlankMap(10, 10);
	
	@Override
	public Game build(){
		Game game = new Game(getID());
		game.setMap(map);
		
		addPlugins(game);
		return game;
	}
	
	// Adding Default Plugins
	public void addPlugins(Game game){
//		game.add(new LogService(gameID+""));
		game.add(new ViewService());
		game.add(new NextService());
		game.add(new MoveService());
		game.add(new GrabService());
		game.add(new DropService());
		game.add(new ScoreService());
		game.add(new CarryingService());
	}
}
