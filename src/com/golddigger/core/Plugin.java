package com.golddigger.core;

public abstract class Plugin {
	public String getName(){
		return this.getClass().getSimpleName();
	}
}
