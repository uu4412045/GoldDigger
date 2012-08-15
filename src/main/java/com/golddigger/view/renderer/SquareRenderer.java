package com.golddigger.view.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.FeatureDescriptor;
import java.net.URL;

import com.golddigger.model.Game;
import com.golddigger.model.Map;
import com.golddigger.model.Tile;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.BaseTile;
import com.golddigger.model.tiles.GoldTile;
import com.golddigger.model.tiles.WallTile;
import com.golddigger.view.FieldView;

public class SquareRenderer implements FieldRenderer {
	public static final String SQUARE_IMAGE_PATH = "../../../images/square/";

	private static Image GOLD0 = null;
	private static Image GOLD1 = loadImage("gold1.png");
	private static Image GOLD2 = loadImage("gold2.png");
	private static Image GOLD3 = loadImage("gold3.png");
	private static Image GOLD4 = loadImage("gold4.png");
	private static Image GOLD5 = loadImage("gold5.png");
	private static Image GOLD6 = loadImage("gold6.png");
	private static Image GOLD7 = loadImage("gold7.png");
	private static Image GOLD8 = loadImage("gold8.png");
	private static Image GOLD9 = loadImage("gold9.png");
	private static Image CENTER = loadImage("center.png");
	private static Image DIGGER = loadImage("digger.png");
	private static Image BANK = loadImage("bank.png");

	private static Image[] golds = new Image[]{GOLD0, GOLD1, GOLD2, GOLD3, GOLD4, GOLD5, GOLD6, GOLD7, GOLD8, GOLD9};

	private Game game;
	private Unit unit;
	private FieldView view;
	
	public SquareRenderer(FieldView view, Game game, Unit unit){
		this.game = game;
		this.unit = unit;
		this.view = view;
	}

	private void render(Graphics graphics){
		int offsetX=0, offsetY=0;
		
		draw(graphics, game.getMap().getTiles());
	}
	
	@Override
	public void render(Graphics graphics, Rectangle bounds) {
		render(graphics);
//		int width = bounds.width / 32;
//		int height = bounds.height / 32;
//		int x = unit.getX() - (width/2);
//		int y = unit.getY() - (height/2);
//		Map map = game.getMap();
//		Tile[][] area = new Tile[width][height];
//		
//		for (int i = 0; i < width; i++){
//			for (int j = 0; j < height; j++)
//				area[i][j] = map.get(x+i, y+i);
//		}
//
//		//TODO: all tiles in area are null
//		draw(graphics, area);
//		draw(graphics, unit, x, y);
	}

	private void draw(Graphics graphics, Unit unit, int offsetX, int offsetY) {
		int x = unit.getX() - offsetX, y = unit.getY() - offsetY;
		graphics.drawImage(DIGGER, x*32, y*32, view);
	}

	private void draw(Graphics g, Tile[][] area){
		for (int i = 0; i < area.length; i++){
			for (int j = 0; j < area[i].length; j++){
				draw(g, area[i][j], i, j);
			}
		}
	}

	//TODO: fix graphics - all tiles being passes in are null
	private void draw(Graphics g, Tile tile, int x, int y) {
		g.drawImage(getImage(tile), x*32, y*32, view);
		g.drawRect(x*32, y*32, 32, 32);
	}

	private static Color color(int x, int y){
		if (x %2 == 0){
			return (y%2==0) ? Color.black : Color.white;
		} else {
			return (y%2==0) ?  Color.white : Color.black;
		}
	}

	private Image getImage(Tile tile){
		Image image = null;
		if (tile instanceof GoldTile){
			GoldTile gold = (GoldTile) tile;
			image = golds[gold.getGold()];
		} else if (tile instanceof BaseTile){
			image = BANK;
		}else if (tile == null){
			image = CENTER;
		}
		return image;
	}


	private static Image loadImage(String name) {
		URL url = FieldView.class.getResource(SQUARE_IMAGE_PATH+name); 
		System.err.println(url.getFile());
		return Toolkit.getDefaultToolkit().getImage(url);
	}
}
