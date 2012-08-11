package com.golddigger.core;

import com.golddigger.core.server.GoldDiggerServer;

public abstract class ServerService extends Service {
	GoldDiggerServer server;
	
	public ServerService(int priority){
		super(priority);
	}
	
	public void setServer(GoldDiggerServer server){
		this.server = server;
	}
}
