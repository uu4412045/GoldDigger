package com.golddigger.server;

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
	private final static String SERVLET_CONTEXT = "golddigger";
	private Server server;
	private String contextID;
	
	public TestServer(){
		try {
			contextID = "localhost:"+PORT+"/"+SERVLET_CONTEXT;
			System.out.println(contextID);
			new AppContext(contextID);
            server = new Server(PORT);
            Context root = new Context(server, "/", Context.SESSIONS);
            root.addServlet(new ServletHolder(new GoldDiggerServlet(contextID)), "/" + SERVLET_CONTEXT + "/*");
//            root.setResourceBase(new File("./target/site").getAbsolutePath());
            root.addServlet(DefaultServlet.class.getName(), "/");
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	public void stop(){
		try {
			server.stop();
			getContext().clear();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args){
		TestServer server = new TestServer();

		server.getContext().add(new BlankGameTemplate());
		server.getContext().add(new BlankGameTemplate());
		
		Player[] players = loadPlayers();
		for (Player player : players){
			server.getContext().add(player);
		}
	}
	
	private static Player[] loadPlayers(){
		return new Player[]{new Player("brett","secret")};
	}
	
	public AppContext getContext(){
		return AppContext.getContext(contextID);
	}
	
	public String getContextID(){
		return contextID;
	}
}
