package com.golddigger.templates;

import java.util.List;

import com.golddigger.core.GameTemplate;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Map;

public class LegacyGameTemplate extends GameTemplate {
	private Map map;
	private List<Service> services;
	
	public LegacyGameTemplate(String result) {}
	
	@Override
	public Game build() {
		Game game = new Game(getID());
		game.setMap(map);
		for (Service service : services){
			game.add(service);
		}
		return game;
	}


}
