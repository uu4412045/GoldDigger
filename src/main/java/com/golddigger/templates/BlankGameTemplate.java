package com.golddigger.templates;

import com.golddigger.model.BlankMap;
import com.golddigger.model.Game;
import com.golddigger.services.GoldService;
import com.golddigger.services.SquareMoveService;
import com.golddigger.services.ViewService;

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
	}
}
