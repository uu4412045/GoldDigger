package com.golddigger.view;

import javax.swing.JLabel;
import javax.swing.JSplitPane;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.server.GameServer;
/**
 * ViewPanel is the container panel that holds both the score and 
 * FieldView. This should only need to be created once.
 * 
 * @author Brett Wandel
 */
public class ViewPanel extends JSplitPane {
	private static final long serialVersionUID = 2129711300758435350L;
	
	/** The panel holding the players score */
	private JLabel score;
	/** The graphical view. This is recreated every time the player progresses. */
	private FieldView field;
	/** The player that is being tracked */
	private Player player;
	/** The GameServer that holds the information */
	private GameServer server;
	/** The current game */
	private Game game;
	
	public ViewPanel(GameServer server, Player player){
		super(JSplitPane.VERTICAL_SPLIT);
		this.player = player;
		this.server = server;
		this.game = server.getGame(player);
		this.score = new JLabel(player.getName()+": 0 [0]");
		this.field = new FieldView(this.game, this.player);
		
		this.setDividerSize(0);
		this.setDividerLocation(25);
		this.setLeftComponent(score);
		this.setRightComponent(field);
		this.setVisible(true);
		
		update();
	}

	/**
	 * @return The player this view is tracking
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Update the display according to any changes that have occurred.
	 */
	public void update(){
		
		Game tmp = server.getGame(player);
		if (tmp == null) {
			System.err.println("ViewPanel: Game is null for player: "+player.getName());
//			Draw Finished Screen
		} else if (tmp != game){
			this.game = tmp;
			this.remove(field);
			this.field = new FieldView(this.game, this.player);
			this.add(field);
		} else {
			Unit unit = game.getUnit(player);
			this.score.setText(player.getName() + ": "+player.getScore()+" ["+unit.getGold()+"]");
		}
		field.repaint();
	}
}
