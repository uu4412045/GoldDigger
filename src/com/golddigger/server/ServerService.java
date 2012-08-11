package com.golddigger.server;


public abstract class ServerService extends Service {
	GoldDiggerServer server;
	
	public ServerService(int priority){
		super(priority);
	}
	
	public void setServer(GoldDiggerServer server){
		this.server = server;
	}
}
