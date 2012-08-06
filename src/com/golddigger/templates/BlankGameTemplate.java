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
	public Game build(String contextID){
		Game game = new Game(getID(), contextID);
		game.setMap(map);
		
		addPlugins(game, contextID);
		return game;
	}
	
	// Adding Default Plugins
	public void addPlugins(Game game, String contextID){
//		game.add(new LogService(gameID+""));
		game.add(new ViewService(contextID));
		game.add(new NextService(contextID));
		game.add(new MoveService(contextID));
		game.add(new GrabService(contextID));
		game.add(new DropService(contextID));
		game.add(new ScoreService(contextID));
		game.add(new CarryingService(contextID));
	}
}
