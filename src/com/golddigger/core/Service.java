package com.golddigger.core;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Base Specification of all plugins
 * @author Brett Wandel
 * @version 0.1
 *
 */
public abstract class Service {
	public static final int BASE_PRIORITY = -1;
	public static final int URL_TARGET = 2,
							URL_PLAYER = 3,
							URL_ACTION = 4,
							URL_EXTRA1 = 5,
							URL_EXTRA2 = 6,
							URL_EXTRA3 = 7;
	
	private int priority = BASE_PRIORITY;
	public int getPriority(){ return this.priority;}
	public Service(int priority){
		this.priority = priority;
	}
	
	public abstract boolean runnable(String url);
	public abstract boolean execute(String url, PrintWriter out);
	public abstract boolean caresAboutConsumption();
	
	/**
	 * Used to accurately decompose the url into its components.
	 * @param url The URL to decompose
	 * @param component The component that you want
	 * @return The value of that component
	 */
	public static String parseURL(String url, int component){
		String[] x = url.split("/"); 
		if (x.length < component+1) return null;
		else return x[component];
	}
}
