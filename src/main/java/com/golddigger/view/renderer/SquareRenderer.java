package com.golddigger.view.renderer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

import com.golddigger.model.Direction;
import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.model.Player;
import com.golddigger.model.Point2D;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.*;
import com.golddigger.view.FieldView;

public class SquareRenderer implements FieldRenderer {
	public static final String SQUARE_IMAGE_PATH = "../../../images/square/";

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
	private static final Image SOLID= loadImage("solid.png");
	private static final int TILE_SIZE = 32;

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
		int y = 32*unit.getX() + 16;
		int x = 32*unit.getY() + 16;
		
		x -= bounds.getWidth()/2;
		y -= bounds.getHeight()/2;
		graphics.translate(-x, -y);
		drawBackground(graphics, bounds);
		draw(graphics, game.getMap().getTiles());
		for (Unit u : game.getUnits()){
			draw(graphics, u);
		}
		graphics.translate(x, y);
	}
	
	private void drawBackground(Graphics g, Rectangle bounds){
		int w = bounds.width+TILE_SIZE, h = bounds.height+TILE_SIZE;
		//Added the bitshifts to round i and j to the closest 32.
		//This aligns the background tiles with the map tiles
		for (int i = (-w>>5)<<5; i < w; i+=TILE_SIZE){
			for (int j = (-h>>5)<<5; j < h; j+=TILE_SIZE){
				g.drawImage(SOLID, i, j, view);
			}
		}
	}

	private void draw(Graphics graphics, Unit unit) {
		//TODO: Fix image: Image is offset by 4 pixels as its not 32x32
		graphics.translate(-4, -4);
		if (unit.isOwnedBy(player)){
			draw(graphics, DIGGER, unit.getX(), unit.getY());
		} else { //Added for new image later
			draw(graphics, DIGGER, unit.getX(), unit.getY());
		}
		graphics.translate(4,4);
	}

	private void draw(Graphics g, Tile[][] area){
		for (int i = 0; i < area.length; i++){
			for (int j = 0; j < area[i].length; j++){
				draw(g, getImage(area[i][j]), i, j);
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
			image = SOLID;
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
		Map map = game.getMap();
		if (map.get(Direction.NORTH.getOffset(position)) instanceof WallTile){
			
		}
		return WALL_CENTER;
	}

	private static Image loadImage(String name) {
		URL url = FieldView.class.getResource(SQUARE_IMAGE_PATH+name); 
		System.err.println(url.getFile());
		return Toolkit.getDefaultToolkit().getImage(url);
	}
}
