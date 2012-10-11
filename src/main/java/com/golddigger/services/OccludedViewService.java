package com.golddigger.services;

import com.golddigger.model.Player;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.OccludedTile;
import com.golddigger.utils.SquareOcclusion;

/**
 * This service will do everything that the {@link ViewService} does, 
 * however this the view also includes occlusion.
 * 
 * @author Brett Wandel
 * @see Player
 * @see Unit
 */
public class OccludedViewService extends ViewService {

	public OccludedViewService(int lineOfSight){
		super(lineOfSight);
	}

	@Override
	public Tile[][] getArea(Unit unit) {
		Tile[][] area = super.getArea(unit);
		//build height map
		int[][] heightMap = new int[area.length][area[0].length];
		for (int i = 0; i < heightMap.length; i++){
			for (int j = 0; j < heightMap[i].length; j++) {
				Tile tile = area[i][j];
				heightMap[i][j] = (tile == null ? -1 : tile.getHeight());
			}
		}
		//build occlusion mask
		boolean[][] mask = SquareOcclusion.buildOcclusionMask(heightMap);
		
		//apply occlusion mask
		for (int i= 0; i < area.length; i ++){
			for (int j = 0; j < area[i].length; j++) {
				if (mask[i][j]) area[i][j] = new OccludedTile();
			}
		}
		return area;
	}
}
