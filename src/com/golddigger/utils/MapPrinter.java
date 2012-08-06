package com.golddigger.utils;

import com.golddigger.model.Map;
import com.golddigger.model.Tile;
import com.golddigger.services.ViewService;

public class MapPrinter {
	public static String print(Map map){
		String result = "";
		for (Tile[] row : map.getTiles()){
			for(Tile t : row){
				result += ViewService.convert(t);
			}
			result+="\n";
		}
		return result;
	}
}
