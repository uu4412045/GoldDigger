package com.golddigger.client;

import java.io.IOException;
import java.net.MalformedURLException;

import org.xml.sax.SAXException;

import com.golddigger.services.MoveService.Direction;
import com.meterware.httpunit.WebConversation;

public class TestingClient {
	private WebConversation wc;
	private String player, baseURL;
	
	public TestingClient(String player, String baseURL){
		this.player = player;
		this.baseURL = baseURL;
		wc = new WebConversation();
	}
	public String carrying(){
		return doGET("/carrying");
	}
	public String score(){
		return doGET("/score");
	}
	public String next(){
		return doGET("/next");
	}
	public String grab(){
		return doGET("/grab");
	}
	public String drop(){
		return doGET("/drop");
	}
	public String view(){
		return doGET("/view");
	}
	public String move(Direction d){
		return doGET("/move/"+d.toString());
	}

	private String doGET(String action){
		try {
			return wc.getResponse(baseURL+"/golddigger/digger/"+player+action).getText();
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
