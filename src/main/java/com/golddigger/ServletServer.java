package com.golddigger;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.servlet.ServletHolder;

import com.golddigger.server.DirectInputDelayedServer;
import com.golddigger.server.GoldDiggerServer;
import com.golddigger.server.GoldDiggerServlet;
import com.golddigger.services.NextService;

public class ServletServer extends GoldDiggerServer {
	private final static int PORT = 8066;
	private final static String SERVLET_CONTEXT = "golddigger";
	private Server server;
	
	public ServletServer(){
		this(null);
	}
	
	public ServletServer(DirectInputDelayedServer delay){
		GoldDiggerServlet servlet = new GoldDiggerServlet(this, delay);
		try {
            server = new Server(PORT);
            Context root = new Context(server, "/", Context.SESSIONS);
            root.addServlet(new ServletHolder(servlet), "/" + SERVLET_CONTEXT + "/*");
            root.addServlet(DefaultServlet.class.getName(), "/");
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		
		this.add(new NextService());
	}

	public void stop(){
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
