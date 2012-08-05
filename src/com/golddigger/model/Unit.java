package com.golddigger.model;

public class Unit {
	private int x, y, gold=0;
	private Player owner;
	
	public Unit(Player owner, int x, int y){
		this.owner = owner;
		this.x = x;
		this.y = y;
		System.out.println("!! New Unit Added");
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Point2D getPosition(){
		return new Point2D(x,y);
	}
	
	public int getX(){ return this.x;}
	public int getY(){ return this.y;}

	public boolean isOwnedBy(Player player) {
		return this.owner == player;
	}
	
	public void setGold(int qty){
		this.gold = qty;
	}
	public int getGold(){
		return this.gold;
	}
}
