package com.golddigger.view.renderer;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.golddigger.model.Game;
import com.golddigger.model.Unit;

public class HexRenderer implements FieldRenderer {
	private Game game;
	private Unit unit;
	
	public HexRenderer(Game game, Unit unit){
		this.game = game;
		this.unit = unit;
	}

	@Override
	public void render(Graphics graphics, Rectangle bounds) {
		graphics.drawString("Hello World", 10, 10);
	}

}
