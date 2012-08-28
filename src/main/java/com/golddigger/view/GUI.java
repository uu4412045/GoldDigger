package com.golddigger.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.golddigger.model.Player;
import com.golddigger.server.GameServer;
/**
 * The GUI is the frame attached to a server that creates
 * a screen for each unit in play.
 * 
 * @author Brett Wandel
 */
public class GUI extends Thread  {
	private GameServer server;
	private Map<Player, ViewPanel> panels = new HashMap<Player, ViewPanel>();
	private JFrame frame;
	private volatile boolean running = false;

	/**
	 * Create a new GUI attached to a GameServer
	 * @param server The server to attach to
	 * @param title The title of the window
	 */
	public GUI(GameServer server, String title){
		this.server = server;
		frame = new JFrame(title);

		frame.setSize(new Dimension(600, 400));
		frame.setVisible(true);
		frame.setLayout(new GridLayout(2,0));
	}

	/**
	 * Close the GUI. Can restart by calling start();
	 */
	public void halt(){
		this.running = false;
	}
	
	@Override
	public void run(){
		running = true;
		while (running){
			for (Player player : server.getPlayers()){
				if (panels.containsKey(player)){
					panels.get(player).update();
				} else {
					if (server.getGame(player) == null) continue;
					ViewPanel view = new ViewPanel(server, player);
					panels.put(player, view);
					frame.add(view);
					frame.validate();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Return the view for a particular player.
	 * @param player The player.
	 * @return The ViewPanel showing their unit and score
	 */
	public ViewPanel getViewFor(Player player) {
		return panels.get(player);
	}

}
