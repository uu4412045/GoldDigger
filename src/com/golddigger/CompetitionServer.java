package com.golddigger;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.servlet.ServletHolder;

import com.golddigger.core.AppContext;
import com.golddigger.core.GameTemplate;
import com.golddigger.core.GoldDiggerServlet;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.utils.CompetitionTemplateGenerator;
import com.golddigger.utils.TemplateGenerator;

public class CompetitionServer {
	private final static int PORT = 8066;
	private final static String CONTEXT = "golddigger";
	private Server server;
	
	public CompetitionServer(){
		try {
            server = new Server(PORT);
            Context root = new Context(server, "/", Context.SESSIONS);
            root.addServlet(new ServletHolder(new GoldDiggerServlet()), "/" + CONTEXT + "/*");
//            root.setResourceBase(new File("./target/site").getAbsolutePath());
            root.addServlet(DefaultServlet.class.getName(), "/");
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	public static void main(String[] args){
		new CompetitionServer();
		TemplateGenerator gen = new CompetitionTemplateGenerator();
		
		GameTemplate template;
		while ((template = gen.next()) != null){
			AppContext.add(template);
		}
		
		Player[] players = loadPlayers();
		for (Player player : players){
			AppContext.add(player);
		}
	}
	
	private static Player[] loadPlayers(){
		return new Player[]{new Player("brett","secret")};
	}
}
