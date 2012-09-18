package com.golddigger.client;

import java.io.IOException;
import java.net.MalformedURLException;

import org.xml.sax.SAXException;

import com.golddigger.model.Direction;
import com.meterware.httpunit.WebConversation;

/**
 * Simple HTML Client for the server, built for automated testing.
 * Basically combination of WebController and AdminWebController, but returns the 
 * http content, rather than the whole request.
 * @author Brett Wandel
 *
 */
public class TestingClient {
	private WebConversation wc;
	private String player, baseURL;
	
	/**
	 * Create a new TestingClient for a particular player.
	 * @param player the players name (used in the url)
	 * @param baseURL the base url of the server (everything before the servlet's context).
	 */
	public TestingClient(String player, String baseURL){
		this.player = player;
		this.baseURL = baseURL;
		wc = new WebConversation();
	}
	/**
	 * check how many gold your unit is carrying
	 * @return the number of gold
	 */
	public String carrying(){
		return sendAction("/carrying");
	}
	
	/**
	 * get your score
	 * @return the number of gold you have banked
	 */
	public String score(){
		return sendAction("/score");
	}
	
	/**
	 * move to the next game
	 * @return "OK" if successful, "FAILED" otherwise
	 */
	public String next(){
		return sendAction("/next");
	}
	
	/**
	 * grab gold on the ground
	 * @return the number of gold you have picked up, "FAILED" you couldn't pick any up.
	 */
	public String grab(){
		return sendAction("/grab");
	}
	
	/**
	 * drop gold on the ground
	 * @return the number of gold you dropped, "FAILED" if you couldn't drop any
	 */
	public String drop(){
		return sendAction("/drop");
	}
	
	/**
	 * Get the diggers view
	 * @return String representation of the view
	 */
	public String view(){
		return sendAction("/view");
	}
	
	/**
	 * Move the digger
	 * @param d the direction you want to move
	 * @return "OK" if successful, "FAILED" otherwise
	 */
	public String move(Direction d){
		return sendAction("/move/"+d.toString());
	}
	
	/**
	 * Buy ammunition for your cannon
	 * @return "OK you have n rounds left" if successful, "FAILED: Dont have enough cash" otherwise.
	 */
	public String buyAmmo(){
		return sendAction("/cannon/buy");
	}
	
	/**
	 * Shoot in a particular direction
	 * @param d The direction to shoot in.
	 * @return "HIT playername" if you hit someone, "MISSED" otherwise
	 */
	public String shoot(Direction d){
		return sendAction("/cannon/shoot/"+d.toString());
	}

	private String sendAction(String action){
		return doGET(baseURL+"/golddigger/digger/"+player+action);
	}
	
	/**
	 * Mainly used to access the admin commands.
	 * @param url The entire url, including "http://"
	 * @return the http content of the response
	 */
	public String doGET(String url){
		try {
			return wc.getResponse(url).getText();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}
}
