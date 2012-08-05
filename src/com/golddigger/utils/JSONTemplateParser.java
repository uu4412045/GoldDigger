package com.golddigger.utils;

import java.io.Reader;

import com.golddigger.core.GameTemplate;
import com.google.gson.Gson;

public class JSONTemplateParser {
	
	public static GameTemplate parse(String json) {
		return new Gson().fromJson(json, GameTemplate.class);
	}
	
	public static GameTemplate parse(Reader reader){
		return new Gson().fromJson(reader, GameTemplate.class);
	}

}
