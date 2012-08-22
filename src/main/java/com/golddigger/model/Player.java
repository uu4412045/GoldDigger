package com.golddigger.model;
/**
 * The class representing a player/team in the server.
 * @author Brett Wandel
 *
 */
public class Player {
	private String name, secret;
	private int score = 0;

	public Player(String name, String secret){
		this.name = name;
		this.secret = secret;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean auth(String guess){
		return this.secret.equalsIgnoreCase(guess);
	}

	public int getScore() {
		return this.score;
	}
	
	public void setScore(int score){
		this.score = score;
	}

	public String getSecret() {
		return secret;
	}

}
