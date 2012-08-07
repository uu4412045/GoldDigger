package com.golddigger.templates;

import java.util.ArrayList;
import java.util.Arrays;

import com.golddigger.core.GameTemplate;
import com.golddigger.core.Service;
import com.golddigger.core.ServiceGenerator;
import com.golddigger.model.Game;
import com.golddigger.utils.MapMaker;
import com.golddigger.utils.generators.BaseServiceGenerator;

public class TestGameTemplate extends GameTemplate {
	private String map;
	private ServiceGenerator gen;
	
	public TestGameTemplate(String map){
		this(map, new BaseServiceGenerator());
	}
	public TestGameTemplate(String map, ServiceGenerator gen) {
		this.map = map;
		this.gen = gen;
	}
	@Override
	public Game build(String contextID) {
		Game game = new Game(getID(), contextID);
		game.setMap(MapMaker.parse(map));
		for (Service service : new ArrayList<Service>(Arrays.asList(gen.generate(contextID)))){
			game.add(service);
		}
		return game;
	}

}
