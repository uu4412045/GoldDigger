package com.golddigger.templates;

import com.golddigger.model.BlankMap;
import com.golddigger.model.Game;
import com.golddigger.server.GameTemplate;
import com.golddigger.services.GoldService;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.ViewService;
import com.golddigger.services.old.CarryingService;
import com.golddigger.services.old.DropService;
import com.golddigger.services.old.GrabService;
import com.golddigger.services.old.ScoreService;

public class BlankGameTemplate extends GameTemplate {

	@Override
	public Game build(){
		Game game = new Game(getID());
		game.setMap(new BlankMap(10, 10));
		
		addServices(game);
		return game;
	}
	
	public void addServices(Game game){
		game.add(new ViewService());
		game.add(new SquareMoveService());
		game.add(new GoldService());
//		game.add(new GrabService());
//		game.add(new DropService());
//		game.add(new ScoreService());
//		game.add(new CarryingService());
	}
}
