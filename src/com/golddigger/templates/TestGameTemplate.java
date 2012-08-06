package com.golddigger.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.golddigger.core.GameTemplate;
import com.golddigger.core.Service;
import com.golddigger.core.ServiceGenerator;
import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.utils.MapMaker;
import com.golddigger.utils.generators.BaseServiceGenerator;

public class TestGameTemplate extends GameTemplate {
	private String map;
	private List<Service> services;
	
	public TestGameTemplate(String map){
		this(map, new BaseServiceGenerator());
	}
	public TestGameTemplate(String map, ServiceGenerator gen) {
		this.map = map;
		this.services = new ArrayList<Service>(Arrays.asList(gen.generate()));
	}
	
	@Override
	public Game build() {
		Game game = new Game(getID());
		game.setMap(MapMaker.parse(map));
		for (Service service : services){
			game.add(service);
		}
		return game;
	}

}
