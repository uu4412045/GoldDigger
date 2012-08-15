package com.golddigger.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.golddigger.model.Player;
import com.golddigger.server.GameServer;

public class GUI extends Thread  {
	private GameServer server;
	private Map<Player, ViewPanel> panels = new HashMap<Player, ViewPanel>();
	private JFrame frame;
	private volatile boolean running = false;

	public GUI(GameServer server, String title){
		this.server = server;
		frame = new JFrame(title);
		//TODO: remove default close operation?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(new Dimension(600, 400));
		frame.setVisible(true);
		frame.setLayout(new GridLayout(2,0));
	}

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
					System.out.println("GUI: Adding "+player.getName());
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

	public ViewPanel getViewFor(Player player) {
		return panels.get(player);
	}

}
