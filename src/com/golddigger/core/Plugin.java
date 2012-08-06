package com.golddigger.core;
/**
 * Unused as of yet.
 * @author Brett Wandel
 *
 */
public abstract class Plugin {
	public String getName(){
		return this.getClass().getSimpleName();
	}
}
