package com.golddigger;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.servlet.ServletHolder;

import com.golddigger.core.AppContext;
import com.golddigger.core.GoldDiggerServer;
import com.golddigger.core.GoldDiggerServlet;

public class GenericServer  extends GoldDiggerServer {
	private final static int PORT = 8066;
	private final static String SERVLET_CONTEXT = "golddigger";
	private Server server;
	private String contextID;
	private AppContext context;
	
	public GenericServer(){
		this(false);
	}
	
	public GenericServer(boolean delayAsWell){
		GoldDiggerServlet servlet = new GoldDiggerServlet(this);
		try {
			contextID = "localhost:"+PORT+"/"+SERVLET_CONTEXT;
			context = new AppContext(contextID);
            server = new Server(PORT);
            Context root = new Context(server, "/", Context.SESSIONS);
            root.addServlet(new ServletHolder(servlet), "/" + SERVLET_CONTEXT + "/*");
            root.addServlet(DefaultServlet.class.getName(), "/");
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	public AppContext getContext(){
		return this.context;
	}
	
	public void stop(){
		try {
			server.stop();
			AppContext.getContext(contextID).clear();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}
