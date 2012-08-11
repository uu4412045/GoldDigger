package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.model.Player;
import com.golddigger.server.GameService;
/**
 * This service will return the players current score.
 * 
 * @author Brett Wandel
 * @deprecated Use {@link GoldService}
 */
public class ScoreService extends GameService {
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
		Player player = game.getPlayer(parseURL(url, URL_PLAYER));
		out.println(""+player.getScore());
		return true;
	}
}
