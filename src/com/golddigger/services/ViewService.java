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
	private int lineOfSight = 1;

	public ViewService(){
		this(1);
	}
	
	public ViewService(int lineOfSight) {
		super(BASE_PRIORITY);
		this.lineOfSight = lineOfSight;
	}
	
	public static String createURL(String player){
		return "/golddigger/digger/"+player+"/view";
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
		Tile[][] area = game.getMap().getArea(unit.getX(), unit.getY(), lineOfSight);

		for (Tile[] row : area){
			for (Tile tile : row){
				out.append(convert(tile));
			}
			out.append('\n');
		}

		return true;
	}
	
	@Override
	public boolean caresAboutConsumption() {
		return true;
	}
	
	/**
	 * Used to convert each tile to their respective character.
	 * @param t The tile to be converted
	 * @return A character representation of that tile
	 */
	private static char convert(Tile t){
		if (t == null) return 'X';
		if (t instanceof WallTile) return 'w';
		if (t instanceof BaseTile) return 'b';
		if (t instanceof CityTile) return 'c';
		if (t instanceof DeepWaterTile) return 'd';
		if (t instanceof ShallowWaterTile) return 's';
		if (t instanceof ForestTile) return 'f';
		if (t instanceof HillTile) return 'h';
		if (t instanceof RoadTile) return 'r';
		if (t instanceof TeleportTile) return 't';
		if (t instanceof GoldTile){
			switch (((GoldTile) t).getGold()){
			case 1: return '1';
			case 2: return '2';
			case 3: return '3';
			case 4: return '4';
			case 5: return '5';
			case 6: return '6';
			case 7: return '7';
			case 8: return '8';
			case 9: return '9';
			default: return '.';
			}
		}
		return '?';
	}

}
