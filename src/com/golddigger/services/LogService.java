package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.Service;

public class LogService extends Service {
	private static final String DEFAULT_PATH = "./logs/";
	private String gameID;
	private PrintWriter out;
	
	
	public LogService(String gameID) {
		super(BASE_PRIORITY);
		this.gameID = gameID;
		this.out = new PrintWriter(System.out, true);
		
		out.println("All Setup");
//		File log = new File(DEFAULT_PATH+gameID+".log");
//		try {
//			if (log.exists()) {
//				log.delete();
//			}
//			log.createNewFile();
//			this.out = new BufferedWriter(new FileWriter(log));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

	@Override
	public boolean runnable(String url) {
		return true;
	}

	@Override
	public boolean execute(String url, PrintWriter out) {
		this.out.println(System.nanoTime()+","+url);
		return false;
	}

	@Override
	public boolean caresAboutConsumption() {
		return false;
	}
}
