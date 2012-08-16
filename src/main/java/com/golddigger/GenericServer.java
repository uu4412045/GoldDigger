package com.golddigger;

import com.golddigger.model.Player;
import com.golddigger.server.DirectInputDelayedServer;
import com.golddigger.server.GoldDiggerServer;
import com.golddigger.services.NextService;
import com.golddigger.templates.GameTemplate;
import com.golddigger.templates.TestGameTemplate;
import com.golddigger.view.GUI;

public class GenericServer {
	private GoldDiggerServer main, delayed;
	private DirectInputDelayedServer delayedServer;
	private GUI mainGUI, delayedGUI;

	/**
	 * Runs a simple server (no delay) in headless mode
	 */
	public GenericServer(){
		this(true);
	}

	public GenericServer(boolean headless){
		this.main = new ServletServer(delayedServer);
		if (!headless){
			this.mainGUI = new GUI(main, "Golddigger");
			this.mainGUI.start();
		}
	}

	public GenericServer(long delay, boolean headless){
		this.delayed = new GoldDiggerServer();
		this.delayedServer = new DirectInputDelayedServer(this.delayed, delay);
		this.delayed.add(new NextService());
		this.main = new ServletServer(this.delayedServer);
		if (!headless){
			this.delayedGUI = new GUI(delayed, "Golddigger - Delayed");
			this.mainGUI = new GUI(main, "Golddigger");
			this.mainGUI.start();
			this.delayedGUI.start();
		}
	}

	public static void main(String[] args){
		GenericServer server = new GenericServer();
		server.addTemplate(new TestGameTemplate("wwwww\nw.b.w\nwwwww"));
		server.addTemplate(new TestGameTemplate("wwwww\nw1b1w\nwwwww"));
		server.addPlayer("test", "secret");
	}

	public GoldDiggerServer getMain(){
		return this.main;
	}

	public GoldDiggerServer getDelayed(){
		return this.delayed;
	}

	public void addPlayer(String name, String secret){
		this.main.add(new Player(name, secret));
		if (this.delayed != null){
			this.delayed.add(new Player(name, secret));
		}
	}

	public void addTemplate(GameTemplate template){
		this.main.add(template);
		if (this.delayed != null){
			this.delayed.add(template);
		}
	}

	public void stop(){
		((ServletServer) main).stop();
		if (mainGUI != null) mainGUI.halt();
		if (delayedGUI != null) delayedGUI.halt();
	}

	public GUI getMainGUI(){return this.mainGUI;}
	public GUI getDelayedGUI(){return this.delayedGUI;}
}
