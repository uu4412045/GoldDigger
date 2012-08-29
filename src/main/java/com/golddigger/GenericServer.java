package com.golddigger;

import com.golddigger.model.Player;
import com.golddigger.server.DirectInputDelayedServer;
import com.golddigger.server.GoldDiggerServer;
import com.golddigger.services.AdminService;
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
	 * @see Mode
	 */
	public GenericServer(){
		this(Mode.HEADLESS);
	}

	//TODO: Refactor this so that setting Server Services is simple
	/**
	 * Runs a simple server in either Headless, or GUI mode.
	 * @param mode Mode.HEADLESS for no gui, Mode.GUI for a gui.
	 * @see Mode
	 */
	public GenericServer(Mode mode){
		this.main = new ServletServer(delayedServer);
		
		this.main.add(new AdminService());
		this.main.add(new NextService());
		
		if (mode == Mode.GUI){
			this.mainGUI = new GUI(main, "Golddigger");
			this.mainGUI.start();
		}
	}

	/**
	 * Starts up a Server with delay and mode.
	 * @param delay the delay time in the delay server (in millisecond).
	 * @param mode Mode.HEADLESS for no gui, Mode.GUI for a gui.
	 */
	public GenericServer(long delay, Mode mode){
		this.delayed = new GoldDiggerServer();
		this.delayedServer = new DirectInputDelayedServer(this.delayed, delay);
		this.main = new ServletServer(this.delayedServer);
		
		this.delayed.add(new NextService());
		this.main.add(new NextService());
		this.delayed.add(new AdminService());
		this.main.add(new AdminService());
		
		if (mode == Mode.GUI){
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
	
	/**
	 * Used to select the visual mode of a server.
	 * @author Brett Wandel
	 *
	 */
	public enum Mode {
		HEADLESS, GUI
	}
}
