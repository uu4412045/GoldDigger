package com.golddigger;

import com.golddigger.model.Player;
import com.golddigger.server.DirectInputDelayedServer;
import com.golddigger.server.GoldDiggerServer;
import com.golddigger.services.NextService;
import com.golddigger.templates.GameTemplate;
import com.golddigger.templates.TestGameTemplate;

public class GenericServer {
	private GoldDiggerServer main, delayed;
	private DirectInputDelayedServer delayedServer;
	
	public GenericServer(){
		this.main = new ServletServer(delayedServer);
	}
	
	public GenericServer(long delay){
		this.delayed = new GoldDiggerServer();
		this.delayedServer = new DirectInputDelayedServer(this.delayed, delay);
		this.delayed.add(new NextService());
		
		this.main = new ServletServer(this.delayedServer);
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
	}
}
