package com.golddigger.view;

import javax.swing.JLabel;
import javax.swing.JSplitPane;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.server.GameServer;

public class ViewPanel extends JSplitPane {
	private static final long serialVersionUID = 2129711300758435350L;
	
	private JLabel score;
	private FieldView field;
	private Player player;
	private GameServer server;
	private Game game;
	
	public ViewPanel(GameServer server, Player player){
		super(JSplitPane.VERTICAL_SPLIT);
		this.player = player;
		this.server = server;
		this.game = server.getGame(player);
		this.score = new JLabel(player.getName()+": 0");
		this.field = new FieldView(this.game, this.player);
		
		this.setDividerSize(0);
		this.setDividerLocation(25);
		this.setLeftComponent(score);
		this.setRightComponent(field);
		this.setVisible(true);
		
		update();
	}

	public Player getPlayer() {
		return player;
	}
	
	public void update(){
		this.score.setText(player.getName() + ": "+player.getScore());
		
		Game tmp = server.getGame(player);
		if (tmp == null) {
			System.err.println("ViewPanel: Game is null for player: "+player.getName());
//			Draw Finished Screen
		} else if (tmp != game){
			this.game = tmp;
			this.remove(field);
			this.field = new FieldView(this.game, this.player);
			this.add(field);
		}
		field.repaint();
	}
}
