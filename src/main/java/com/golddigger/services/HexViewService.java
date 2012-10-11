package com.golddigger.services;

import java.io.PrintWriter;
import java.util.Arrays;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.OccludedTile;
import com.golddigger.utils.JsonEncoder;
import com.golddigger.utils.MapMaker;

public class HexViewService extends ViewService {
	private static int TRUE=1, CHECK=2, CHECKED=3;

	public HexViewService() {
		super();
	}
	
	public HexViewService(int lineOfSight){
		super(lineOfSight);
	}
	
	@Override
	public Tile[][] getArea(Unit unit){
		Tile[][] area = game.getMap().getArea(unit.getPosition(), getLineOfSight());
		int[][] hex = mask((area.length-1)/2, unit);
		
		for (int i = 0; i < area.length; i++) {
			for (int j = 0; j < area[i].length; j++) {
				if (area[i][j] == null) continue;
				if (hex[i][j] < 1) {
					area[i][j] = new OccludedTile();
				}
			}
		}
		return area;
	}
	
	public static int[][] mask(int radius, Unit unit){
		int size = (radius*2)+1;
		int[][] mask = new int[size][size];
		mask[radius][radius] = CHECK;

		for (int i = 0; i < radius; i++){
			for (int lat = 0; lat < size; lat++){
				for (int lng = 0; lng < size; lng++) {
					if (mask[lat][lng] == CHECK) mask = markNeighbours(mask,lat,lng,unit);
				}
			}
			
			for (int lat = 0; lat < mask.length; lat++) {
				for (int lng = 0; lng < mask[lat].length; lng++) {
					if (mask[lat][lng] == TRUE) {
						mask[lat][lng] = CHECK;
					}
				}				
			}
		}

		return mask;
	}
	
	private static int[][] markNeighbours(int[][] area, int lat, int lng, Unit unit){
		int radius = (area.length-1)/2;
		int ref_lng = unit.getLng() + lng - radius;
		if (area.length == 3) ref_lng = unit.getLng();
		
		area[lat][lng] = CHECKED;
		if (area[lat+1][lng] == 0) area[lat+1][lng] = TRUE;
		if (area[lat-1][lng] == 0) area[lat-1][lng] = TRUE;
		if (area[lat][lng+1] == 0) area[lat][lng+1] = TRUE;
		if (area[lat][lng-1] == 0) area[lat][lng-1] = TRUE;
		
		if (ref_lng % 2 == 0){
			if (area[lat-1][lng+1] == 0) area[lat-1][lng+1] = TRUE;
			if (area[lat-1][lng-1] == 0) area[lat-1][lng-1] = TRUE;			
		} else {
			if (area[lat+1][lng+1] == 0) area[lat+1][lng+1] = TRUE;
			if (area[lat+1][lng-1] == 0) area[lat+1][lng-1] = TRUE;			
		}
		return area;
	}

}
