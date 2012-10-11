package com.golddigger.services;

import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.OccludedTile;
import com.golddigger.utils.HexOcclusion;

/**
 * The occluded hex view simply adds "height-based" (tile height) occlusion
 * to the normal HexView.
 * 
 * @author Brett Wandel
 *
 */
public class OccludedHexViewService extends HexViewService {

	public OccludedHexViewService(int lineOfSight){
		super(lineOfSight);
	}
	
	@Override
	public Tile[][] getArea(Unit unit) {
		Tile[][] area = super.getArea(unit);
		//Build the height map
		int[][] heightMap = new int[area.length][area[0].length];
		for (int i = 0; i < heightMap.length; i++){
			for (int j = 0; j < heightMap[i].length; j++) {
				Tile tile = area[i][j];
				heightMap[i][j] = (tile == null ? -1 : tile.getHeight());
			}
		}
		// build the occlusion mask
		boolean[][] mask = HexOcclusion.create(heightMap, unit.getPosition());
		// apply the occlusion mask
		for (int i= 0; i < area.length; i ++){
			for (int j = 0; j < area[i].length; j++) {
				if (mask[i][j]) area[i][j] = new OccludedTile();
			}
		}
		return area;
	}
}
