package com.golddigger.core;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.golddigger.model.Game;
import com.golddigger.model.Player;

public class GoldDiggerServlet extends HttpServlet {
	private static final long serialVersionUID = 1219399141770957347L;
	private String contextID;
	
	public GoldDiggerServlet(String contextID){
		super();
		this.contextID = contextID;
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURI();
		String name = Service.parseURL(url, Service.URL_PLAYER);
		Player player = AppContext.getContext(contextID).getPlayer(name);
		if (player == null) {
			System.err.println("Url with invalid player recieved: "+url);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		Game game = AppContext.getContext(contextID).getGame(player);
		if (game == null) {
			System.err.println("This player does not have a game: "+url);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		process(url, resp.getWriter(), player, game);
	}
	
	public void process(String url, PrintWriter out, Player player, Game game){
		/*
		 * This block decides what services are run, as well as stopping
		 * people running parallel requests.
		 */
		synchronized (player) {
			Service[] services = game.getServices();
			boolean consumed = false;
			for (Service service : services){
				if (service.caresAboutConsumption() && consumed){
					break; //skip this service
				} else if (service.runnable(url)){
					consumed = service.execute(url, out);
				}
			}
		}
	}
}
