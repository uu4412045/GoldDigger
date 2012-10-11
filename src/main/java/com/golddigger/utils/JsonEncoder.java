package com.golddigger.utils;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Coordinate;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.OccludedTile;
/**
 * The JsonEncoder converts our simple model objects into JSON for the view command.
 * @author Brett Wandel
 */
public class JsonEncoder {
	/**
	 * Convert a tile into JSON
	 * @param tile the tile to convert
	 * @return {"tile":{"type":"."}}<br />
	 * {"tile":{"type":"out-of-bounds"}}<br />
	 * {"tile":{"type":"unknown"}}<br />
	 */
	public static String encodeTile(Tile tile){
		String res = "{\"tile\":{\"type\":\"";
		if (tile == null) res += "out-of-bounds";
		else if (tile instanceof OccludedTile) res += "unknown";
		else res += MapMaker.convert(tile);
		return res + "\"}}";
	}

	/**
	 * Convert a unit into JSON
	 * @param unit the tile to convert
	 * @param centre the centre of the view (for correct output coordinate)
	 * @return {"diiger":{"position":"(lat,lng)", "owner":"brett"}}
	 */
	public static String encodeUnit(Unit unit, Coordinate centre){
		Coordinate pos = unit.getPosition().sub(centre);
		return "{\"digger\":{\"position\":\""+pos.toString()+"\",\"owner\":\""+unit.getOwner().getName()+"\"}}";
	}

	/**
	 * Convert all the tiles in the area
	 * @param area the view of the player
	 * @return "tiles":[[encodeTile(),encodeTile()],[encodeTile(), encodeTile()]]
	 */
	public static String encodeArea(Tile[][] area){
		String res = "\"tiles\":[";
		for (int i = 0; i < area.length; i++){
			res += "[";
			for (int j = 0; j < area.length; j++){
				res += encodeTile(area[i][j]);
				if (j+1 != area.length) res+=",";
				else res += "]";
			}
			if (i+1 != area.length) res+=",";
			else res += "]";
		}
		return res;
	}
	
	/**
	 * Convert all the units in the area
	 * @param game The game the player is in
	 * @param area the view of the player
	 * @param centre the centre of the view (for correct output coordinate)
	 * @return "diggers":[encodeUnit(), encodeUnit()]
	 */
	public static String encodeUnits(Game game, Tile[][] area, Coordinate centre){
		String output = "\"diggers\":[";
		for (Unit unit : game.getUnits()){
			Coordinate pos = unit.getPosition().sub(centre);
			if (pos.lat == 0 && pos.lng == 0) continue; //the players digger
			if (pos.lat < 0 || pos.lat >= area.length || pos.lng < 0 || pos.lng >= area[pos.lat].length) continue;
			else output += encodeUnit(unit, centre) + ",";
		}
		if (output.length() != 12) output = output.substring(0, output.length()-1); 
		return output+"]";
	}

	/**
	 * Convert all the objects in the area
	 * @param game The game the player is in
	 * @param area the view of the player
	 * @param player the actual player
	 * @return {encodeArea(), encodeUnits()}
	 */
	public static String encode(Game game, Tile[][] area, Player player){
		Coordinate centre = game.getUnit(player).getPosition();
		String output = "{";
		output += encodeArea(area);
		output += ",";
		output += encodeUnits(game, area, centre);
		return output + "}";
	}
}
