package com.golddigger.view.renderer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

import com.golddigger.model.Game;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.*;
import com.golddigger.view.FieldView;

public class SquareRenderer implements FieldRenderer {
	public static final String SQUARE_IMAGE_PATH = "/images/square/";

	private static final int TILE_SIZE = 32;
	private static final Image GOLD0 = loadImage("empty.png");
	private static final Image GOLD1 = loadImage("gold1.png");
	private static final Image GOLD2 = loadImage("gold2.png");
	private static final Image GOLD3 = loadImage("gold3.png");
	private static final Image GOLD4 = loadImage("gold4.png");
	private static final Image GOLD5 = loadImage("gold5.png");
	private static final Image GOLD6 = loadImage("gold6.png");
	private static final Image GOLD7 = loadImage("gold7.png");
	private static final Image GOLD8 = loadImage("gold8.png");
	private static final Image GOLD9 = loadImage("gold9.png");
	private static final Image WALL_CENTER = loadImage("center.png");
	private static final Image DIGGER = loadImage("digger.png");
	private static final Image BANK = loadImage("bank.png");
	private static final Image CITY = loadImage("city.png");
	private static final Image DEEP_WATER = loadImage("deep_water.png");
	private static final Image FOREST = loadImage("forest.png");
	private static final Image HILL = loadImage("hill.png");
	private static final Image MOUNTAIN = loadImage("mountain.png");
	private static final Image ROAD = loadImage("road.png");
	private static final Image SHALLOW_WATER = loadImage("shallow_water.png");
	private static final Image TELEPORT= loadImage("teleport.png");
	private static final Image WALL_SOLID = loadImage("solid.png");
	private static final Image WALL_NORTH = loadImage("w_north.png");
	private static final Image WALL_SOUTH = loadImage("w_south.png");
	private static final Image WALL_EAST = loadImage("w_east.png");
	private static final Image WALL_WEST = loadImage("w_west.png");
	private static final Image WALL_NORTHEAST = loadImage("w_northeast.png");
	private static final Image WALL_SOUTHEAST = loadImage("w_southeast.png");
	private static final Image WALL_NORTHWEST = loadImage("w_northwest.png");
	private static final Image WALL_SOUTHWEST = loadImage("w_southwest.png");
	private static final Image WALL_NORTHEAST_INV = loadImage("w_northeast_i.png");
	private static final Image WALL_SOUTHEAST_INV = loadImage("w_southeast_i.png");
	private static final Image WALL_NORTHWEST_INV = loadImage("w_northwest_i.png");
	private static final Image WALL_SOUTHWEST_INV = loadImage("w_southwest_i.png");

	private static Image[] golds = new Image[]{GOLD0, GOLD1, GOLD2, GOLD3, GOLD4, GOLD5, GOLD6, GOLD7, GOLD8, GOLD9};

	private Game game;
	private Player player;
	private FieldView view;
	
	public SquareRenderer(FieldView view, Game game, Player player){
		this.game = game;
		this.player = player;
		this.view = view;
	}
	
	@Override
	public void render(Graphics graphics, Rectangle bounds) {
		Unit unit = game.getUnit(player);
		int y = 32*unit.getLat() + 16;
		int x = 32*unit.getLng() + 16;
		x -= bounds.getWidth()/2;
		y -= bounds.getHeight()/2;
		drawBackground(graphics, bounds);
		
		graphics.translate(-x, -y);
		draw(graphics, game.getMap().getTiles());
		for (Unit u : game.getUnits()){
			draw(graphics, u);
		}
		graphics.translate(x, y);
	}
	
	private void drawBackground(Graphics g, Rectangle bounds){
		int w = bounds.width, h = bounds.height;
		//Added the bitshifts to round i and j to the closest 32.
		//This aligns the background tiles with the map tiles
		for (int i = (-w>>5)<<5; i < w; i+=TILE_SIZE){
			for (int j = (-h>>5)<<5; j < h; j+=TILE_SIZE){
				g.drawImage(WALL_SOLID, i, j, view);
			}
		}
	}

	private void draw(Graphics graphics, Unit unit) {
		//TODO: Fix image: Image is offset by 4 pixels as its not 32x32
		graphics.translate(-4, -4);
		if (unit.isOwnedBy(player)){
			draw(graphics, DIGGER, unit.getLat(), unit.getLng());
		} else { //Added for new image later
			draw(graphics, DIGGER, unit.getLat(), unit.getLng());
		}
		graphics.translate(4,4);
	}

	private void draw(Graphics g, Tile[][] area){
		for (int lat = 0; lat < area.length; lat++){
			for (int lng = 0; lng < area[lat].length; lng++){
				draw(g, getImage(area[lat][lng]), lat, lng);
			}
		}
	}

	private void draw(Graphics g, Image image, int x, int y){
		g.drawImage(image, y*TILE_SIZE ,x*TILE_SIZE, view);
	}
	
	private Image getImage(Tile tile){
		Image image = null;
		if (tile instanceof GoldTile){
			GoldTile gold = (GoldTile) tile;
			image = golds[gold.getGold()];
		} else if (tile instanceof BaseTile){
			image = BANK;
		} else if (tile == null){
			image = WALL_SOLID;
		} else if (tile instanceof CityTile){
			image = CITY;
		} else if (tile instanceof DeepWaterTile){
			image = DEEP_WATER;
		} else if (tile instanceof ForestTile){
			image = FOREST;
		} else if (tile instanceof HillTile){
			image = HILL;
		} else if (tile instanceof MountainTile){
			image = MOUNTAIN;
		} else if (tile instanceof RoadTile){
			image = ROAD;
		} else if (tile instanceof ShallowWaterTile){
			image = SHALLOW_WATER;
		} else if (tile instanceof TeleportTile){
			image = TELEPORT;
		} else if (tile instanceof WallTile){
			image = getWallTileImage(game.getMap().getPosition(tile));
		}
		return image;
	}

	private Image getWallTileImage(Point2D position) {
		Tile[][] area = game.getMap().getArea(position, 1);
		if (wallIs(area, MASK_SOLID)) return WALL_SOLID;
		if (wallIs(area, MASK_NORTH)) return WALL_NORTH;
		if (wallIs(area, MASK_SOUTH)) return WALL_SOUTH;
		if (wallIs(area, MASK_EAST)) return WALL_EAST;
		if (wallIs(area, MASK_WEST)) return WALL_WEST;
		if (wallIs(area, MASK_NORTHEAST)) return WALL_NORTHEAST;
		if (wallIs(area, MASK_SOUTHEAST)) return WALL_SOUTHEAST;
		if (wallIs(area, MASK_NORTHWEST)) return WALL_NORTHWEST;
		if (wallIs(area, MASK_SOUTHWEST)) return WALL_SOUTHWEST;
		if (wallIs(area, MASK_NORTHEAST_INV)) return WALL_NORTHEAST_INV;
		if (wallIs(area, MASK_SOUTHEAST_INV)) return WALL_SOUTHEAST_INV;
		if (wallIs(area, MASK_NORTHWEST_INV)) return WALL_NORTHWEST_INV;
		if (wallIs(area, MASK_SOUTHWEST_INV)) return WALL_SOUTHWEST_INV;
		return WALL_CENTER;
	}

	private static Image loadImage(String name) {
		URL url = SquareRenderer.class.getResource(SQUARE_IMAGE_PATH+name);
		return Toolkit.getDefaultToolkit().getImage(url);
	}

	private boolean wallIs(Tile[][] area, int[][] mask){
		for (int lat=0; lat < 3; lat++){
			for (int lng = 0; lng < 3; lng++){
				int a = mask[lat][lng];
				if (a == ANY) continue;
				
				if (area[lat][lng] == null || area[lat][lng] instanceof WallTile) {
					if (a != WALL) return false;
				} else if (a == WALL) return false;
			}
		}
		return true;
	}
	
	private static final int WALL=0, NOTW=1, ANY=2;
	private static final int[][] MASK_SOLID = new int[][] {
		{WALL, WALL, WALL},
		{WALL, WALL, WALL},
		{WALL, WALL, WALL},
	};
	
	private static final int[][] MASK_NORTH = new int[][] {
		{ANY,  WALL, ANY },
		{WALL, WALL, WALL},
		{ANY,  NOTW, ANY },
	};
	
	private static final int[][] MASK_NORTHEAST = new int[][] {
		{WALL,  WALL, WALL},
		{WALL,  WALL, WALL},
		{NOTW,  WALL, WALL},
	};
	
	private static final int[][] MASK_NORTHEAST_INV = new int[][] {
		{ANY,  NOTW, ANY},
		{WALL, WALL, NOTW},
		{WALL, WALL, ANY},
	};
	
	private static final int[][] MASK_EAST = new int[][] {
		{ANY,  WALL, ANY },
		{NOTW, WALL, WALL},
		{ANY,  WALL, ANY },
	};
	
	private static final int[][] MASK_SOUTHEAST = new int[][] {
		{NOTW, WALL, WALL},
		{WALL, WALL, WALL},
		{WALL, WALL, WALL},
	};
	
	private static final int[][] MASK_SOUTHEAST_INV = new int[][] {
		{WALL, WALL, ANY},
		{WALL, WALL, NOTW},
		{ANY,  NOTW, ANY },
	};
	
	private static final int[][] MASK_SOUTH = new int[][] {
		{ANY,  NOTW, ANY},
		{WALL, WALL, WALL},
		{ANY,  WALL, ANY},
	};
	
	private static final int[][] MASK_SOUTHWEST = new int[][] {
		{WALL, WALL, NOTW},
		{WALL, WALL, WALL},
		{WALL, WALL, WALL},
	};
	
	private static final int[][] MASK_SOUTHWEST_INV = new int[][] {
		{ANY,  WALL, WALL },
		{NOTW, WALL, WALL },
		{ANY,  NOTW, ANY },
	};
	
	private static final int[][] MASK_WEST = new int[][] {
		{ANY,  WALL, ANY },
		{WALL, WALL, NOTW},
		{ANY,  WALL, ANY },
	};
	
	private static final int[][] MASK_NORTHWEST = new int[][] {
		{WALL, WALL, WALL },
		{WALL, WALL, WALL },
		{WALL, WALL, NOTW },
	};
	
	private static final int[][] MASK_NORTHWEST_INV = new int[][] {
		{ANY,  NOTW, ANY },
		{NOTW, WALL, WALL },
		{ANY,  WALL, WALL },
	};
		
}
