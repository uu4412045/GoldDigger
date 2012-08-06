package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Player;
/**
 * This service will return the players current score.
 * 
 * @author Brett Wandel
 */
public class ScoreService extends Service {
	public static final String ACTION_TEXT = "score";
	public ScoreService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		Player player = AppContext.getPlayer(parseURL(url, URL_PLAYER));
		if (player == null){
			out.println("ERROR: Invalid Player Given");
			return true;
		}

		out.println(""+player.getScore());
		return true;
	}
}
