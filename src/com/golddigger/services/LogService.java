package com.golddigger.services;

import java.io.PrintWriter;

import com.golddigger.core.Service;
import com.golddigger.model.Player;
import com.golddigger.model.Unit;
import com.golddigger.model.tiles.GoldTile;
/**
 * This service logs all the incoming request URLs to a file which can be used to run a time-delayed server.
 * @author Brett Wandel
 */
public class LogService extends Service {
//	private static final String DEFAULT_PATH = "./logs/";
	private String gameID;
	private PrintWriter out;
	
	
	public LogService(String contextID, String gameID) {
		super(BASE_PRIORITY, contextID);
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
