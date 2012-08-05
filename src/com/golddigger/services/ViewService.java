package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;
import com.golddigger.core.Service;
import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.*;

public class ViewService extends Service {
	public static final String ACTION_TEXT = "view";
	public static final int LINE_OF_SIGHT = 1;

	public ViewService() {
		super(BASE_PRIORITY);
	}

	@Override
	public boolean runnable(String url) {
		return parseURL(url, URL_ACTION).equalsIgnoreCase(ACTION_TEXT);
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		System.out.println("Executing View Service");

		System.out.println("  => Getting Player");
		Player player = AppContext.getPlayer(parseURL(url, URL_PLAYER));
		if (player == null){
			out.println("FAILED: Invalid Player Given");
			return true;
		}

		System.out.println("  => Getting Game");
		Game game = AppContext.getGame(player);
		if (game == null){
			out.println("FAILED: Player is currently not in a game");
			return true;
		}
		
		System.out.println("  => Getting Unit");
		Unit unit = game.getUnit(player);
		if (unit == null){
			out.println("FAILED: no unit found for this player");
			return true;
		}

		System.out.println("  ==> Getting Area");
		Tile[][] area = game.getMap().getArea(unit.getX(), unit.getY(), LINE_OF_SIGHT);

		for (Tile[] row : area){
			for (Tile tile : row){
				if (tile == null) out.append('X');
				else if (tile instanceof GoldTile){
					GoldTile goldTile = (GoldTile) tile;
					if (goldTile.getGold() == 0) out.append('.');
					else out.append(goldTile.getGold()+"");
				}
				else if (tile instanceof BaseTile) out.append('b');
				else out.append('?');
			}
			out.append('\n');
		}

		return true;
	}

	@Override
	public boolean caresAboutConsumption() {
		// TODO Auto-generated method stub
		return false;
	}

}
