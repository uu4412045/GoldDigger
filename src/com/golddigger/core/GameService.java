package com.golddigger.core;

import com.golddigger.model.Game;

public abstract class GameService extends Service {
	protected Game game;
	
	public GameService(int priority) {
		super(priority);
	}
	
	public void setGame(Game game){
		this.game = game;
	}
}
