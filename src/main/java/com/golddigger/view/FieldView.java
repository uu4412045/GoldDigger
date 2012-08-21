package com.golddigger.view;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.services.HexMoveService;
import com.golddigger.view.renderer.FieldRenderer;
import com.golddigger.view.renderer.HexRenderer;
import com.golddigger.view.renderer.SquareRenderer;

/**
 * A frame that is responsible for drawing the Field in the GUI.
 * Each new game (when a player progresses) generates a new FieldView.
 * That is, each FieldView is dedicated to a single game.
 * @author Brett Wandel
 *
 */
public class FieldView extends JPanel {
	private static final long serialVersionUID = 8697274398234293074L;
	private FieldRenderer renderer;

	public FieldView(Game game, Player player) {
		boolean isHex = game.getServices(HexMoveService.class).size() == 1;
		
		if (isHex) {
			renderer = new HexRenderer(this, game, player);
		} else {
			renderer = new SquareRenderer(this, game, player);
		}
		
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		renderer.render(g, this.getBounds());
	}
	
	
}
