package com.golddigger.server.delayed;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import com.golddigger.server.GoldDiggerServer;
import com.golddigger.utils.tools.ContinuousFileReader;

public class FileDelayedServer extends DelayedServer {
	File log;
	BufferedReader reader;
	public FileDelayedServer(GoldDiggerServer server, long delay, File log) {
		super(server, delay);
		try {
			reader = new ContinuousFileReader(log);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileDelayedServer(GoldDiggerServer server, long delay, String path){
		this(server, delay, new File(path));
	}
	
	@Override
	protected String next() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
