package com.golddigger.templates;


import com.golddigger.model.Game;
import com.golddigger.services.GameService;
import com.golddigger.services.generators.BaseServiceGenerator;
import com.golddigger.services.generators.ServiceGenerator;
import com.golddigger.utils.MapMaker;

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
	public Game build() {
		Game game = new Game(getID());
		game.setMap(MapMaker.parse(map));
		for (GameService service : gen.generate()){
			game.add(service);
		}
		return game;
	}
}
