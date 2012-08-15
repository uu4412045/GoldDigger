package com.golddigger.view;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.services.HexMoveService;
import com.golddigger.view.renderer.FieldRenderer;
import com.golddigger.view.renderer.HexRenderer;
import com.golddigger.view.renderer.SquareRenderer;

public class FieldView extends JPanel {
	
	private FieldRenderer renderer;
	private boolean isHex;
	private Game game;
	private Unit unit;

	public FieldView(Game game, Player player) {
		System.out.println("drawing new Game");
		this.game = game;
		this.unit = game.getUnit(player);
		
		isHex = game.getServices(HexMoveService.class).size() == 1;
		if (isHex) renderer = new HexRenderer(game, unit);
		else renderer = new SquareRenderer(this, game, unit);
		this.setVisible(true);
	}

//	private FieldPeek createFieldPeek(Unit unit) {
//		return new FieldPeek(game, unit, Math.round(getWidth()/32F), Math.round(getHeight()/32F));
//	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		renderer.render(g, this.getBounds());
	}
	
	
}
