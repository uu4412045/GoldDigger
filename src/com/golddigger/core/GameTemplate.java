package com.golddigger.core;

import com.golddigger.model.Game;

public abstract class GameTemplate {
	private int gameID = -1;
	public void setID(int id){this.gameID = id;}
	public int getID(){return this.gameID;}
	public abstract Game build();
}
