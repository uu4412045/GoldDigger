package com.golddigger;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.servlet.ServletHolder;

import com.golddigger.core.AppContext;
import com.golddigger.core.GoldDiggerServlet;
import com.golddigger.model.Player;
import com.golddigger.templates.BlankGameTemplate;

public class TestServer {
	private final static int PORT = 8066;
	private final static String CONTEXT = "golddigger";
	private Server server;
	
	public TestServer(){
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
		new TestServer();

		AppContext.add(new BlankGameTemplate(1));
		AppContext.add(new BlankGameTemplate(2));
		
		Player[] players = loadPlayers();
		for (Player player : players){
			AppContext.add(player);
		}
	}
	
	private static Player[] loadPlayers(){
		return new Player[]{new Player("brett","secret")};
	}
}
