package com.golddigger.tools;

import com.golddigger.model.Direction;

public class TestingClient {
	private TestingServer server;
	private String player;
	private static final String base = "http://localhost:8066/golddigger/digger/";
	
	public TestingClient(TestingServer server, String player){
		this.server = server;
		this.player = player;
	}
	
	public String carrying(){
		return send("carrying");
	}
	
	public String score(){
		return send("next");
	}
	
	public String grab(){
		return send("grab");
	}
	
	public String drop(){
		return send("drop");
	}
	
	public String view(){
		return send("view");
	}
	
	public String move(Direction d){
		return send("move/"+d.toString());
	}
	
	private String send(String action){
		return server.execute(base + player+"/"+action);
	}
}
