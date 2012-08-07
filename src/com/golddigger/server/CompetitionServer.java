package com.golddigger.server;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.servlet.ServletHolder;

import com.golddigger.core.AppContext;
import com.golddigger.core.GameTemplate;
import com.golddigger.core.GoldDiggerServlet;
import com.golddigger.model.Player;
import com.golddigger.utils.generators.CompetitionTemplateGenerator;
import com.golddigger.utils.generators.TemplateGenerator;

public class CompetitionServer {
	private final static int PORT = 8066;
	private final static String CONTEXT = "golddigger";
	private Server server;
	private String contextID;
	private AppContext context;
	
	public CompetitionServer(int port){
		try {
            server = new Server(port);
            contextID = "localhost:"+port;
            context = new AppContext(contextID);
            Context root = new Context(server, "/", Context.SESSIONS);
            root.addServlet(new ServletHolder(new GoldDiggerServlet(CONTEXT)), "/" + CONTEXT + "/*");
            root.addServlet(DefaultServlet.class.getName(), "/");
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	public static void main(String[] args){
		CompetitionServer server = new CompetitionServer(PORT);
		TemplateGenerator gen = new CompetitionTemplateGenerator();
		
		GameTemplate template;
		while ((template = gen.next()) != null){
			server.getContext().add(template);
		}
		
		Player[] players = loadPlayers();
		for (Player player : players){
			server.getContext().add(player);
		}
	}
	
	private static Player[] loadPlayers(){
		return new Player[]{new Player("brett","secret")};
	}
	
	public AppContext getContext(){
		return context;
	}
}
