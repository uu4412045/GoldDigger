package com.golddigger.server.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.golddigger.server.GoldDiggerServer;
import com.golddigger.server.Service;

/**
 * The Servlet for the main interface for the Competitors. Simply passes the url to the {@link GoldDiggerServlet}
 * @author Brett Wandel
 */
public class GoldDiggerServlet extends HttpServlet{
	private static final long serialVersionUID = 1219399141770957347L;
	private GoldDiggerServer server;
	private DirectInputDelayedServer delayedServer;
    private Set<String> executingSecrets = new HashSet<String>();
	
	public GoldDiggerServlet(GoldDiggerServer server){
		super();
		this.server = server;
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURL().toString();
		String sync = Service.parseURL(url, Service.URL_PLAYER);
		if (sync == null) sync = "defaultSyncString";
		try {
			synchronized (executingSecrets) {
                if(executingSecrets.contains(sync)) {
                    resp.sendError(503);
                    return;                        
                } else {
                   executingSecrets.add(sync);
                }
            }
			synchronized(sync) {
                long sleep = 100;
                // long sleep = 20; // The sleep value is only used as a load limiter, no client should be able to overload the server. Change it if you want to test and is in a hurry
                String header = req.getHeader("sleep");
                if (header != null) {
                    try {
                        sleep = Long.parseLong(header);
                    } catch (NumberFormatException e) {
                        // never mind
                    }
                }
                Thread.sleep(sleep);
            }
			if (delayedServer != null) delayedServer.add(url);
			server.process(url, resp.getWriter());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executingSecrets.remove(sync);
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}

	public void setDelayedServer(DirectInputDelayedServer delayedServer) {
		this.delayedServer = delayedServer;
	}
}
