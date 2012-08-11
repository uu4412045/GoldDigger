package com.golddigger.templates;


import com.golddigger.core.GameService;
import com.golddigger.core.GameTemplate;
import com.golddigger.model.Game;
import com.golddigger.utils.MapMaker;
import com.golddigger.utils.generators.BaseServiceGenerator;
import com.golddigger.utils.generators.ServiceGenerator;

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
