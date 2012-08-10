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

	@Override
	public Game build(String contextID){
		Game game = new Game(getID(), contextID);
		game.setMap(new BlankMap(10, 10));
		
		addServices(game);
		return game;
	}
	
	public void addServices(Game game){
		game.add(new ViewService());
		game.add(new NextService());
		game.add(new MoveService());
		game.add(new GrabService());
		game.add(new DropService());
		game.add(new ScoreService());
		game.add(new CarryingService());
	}
}
