package com.golddigger.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Servlet for the main interface for the Competitors. Simply passes the url to the {@link GoldDiggerServlet}
 * @author Brett Wandel
 */
public class GoldDiggerServlet extends HttpServlet{
	private static final long serialVersionUID = 1219399141770957347L;
	private GoldDiggerServer server;
	private DirectInputDelayedServer delayedServer;
	
	public GoldDiggerServlet(GoldDiggerServer server){
		super();
		this.server = server;
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURL().toString();
		url = url.substring(url.indexOf("//")+2); // remove the "http://" from the url
		
		if (delayedServer != null) delayedServer.add(DelayedServer.buildEntry("delay-"+url));
		server.process(url, resp.getWriter());
	}

	public void setDelayedServer(DirectInputDelayedServer delayedServer) {
		this.delayedServer = delayedServer;
	}
}
