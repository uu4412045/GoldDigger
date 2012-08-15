package com.golddigger.utils;

import com.golddigger.templates.CustomizableGameTemplate;
import com.golddigger.templates.GameTemplate;

public class LegacyTemplateParser {
	public static final String DELIMITER = "!", SEPERATOR = "=";

	public final static String TILES = "field-tiles",
			COSTS = "cost-per-type",
			LINE_OF_SIGHT = "line-of-sight",
			NO_OF_SIDES   = "number-of-sides",
			PLUGINS = "plugins";

	public static GameTemplate parse(String text){
		CustomizableGameTemplate template = new CustomizableGameTemplate();
		if (text.contains(DELIMITER)){
			template.setMap(getSection(TILES, text));
			
			String[] costs = parseCosts(text);
			if (costs != null) template.setCosts(costs);

			int los = getLineOfSight(text);
			if (los != -1){
				template.setLineOfSight(los);
			}
		} else {
			template.setMap(text);
		}
		return template;
	}

	public static String[] parseCosts(String text){
		String section = getSection(COSTS, text);
		if (section == null) return null;
		String[] costs = section.split("\n");
		return costs;
	}
	
	/**
	 * Returns the line of sight length from the field file
	 * If not found, will set it to the default line of sight length
	 */
	public static int getLineOfSight(String text) {
		String value = getAttribute(LINE_OF_SIGHT, text);
		if (value == null) return -1;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e){
			e.printStackTrace();
			return -1;
		}
	}

	/** 
	 * parses the "results" variable to retrieve a particular section.
	 * The start of a new section is denoted with the DELIMITER
	 * @param title The section name, with out the delimiter to look for.
	 * @return The String between the section name and the next delimiter
	 */
	public static String getSection(String title, String text){
		if (!text.contains(DELIMITER+title)) {
			return null;
		}

		int start = text.indexOf(title)+title.length();
		int end = text.indexOf(DELIMITER, start);

		String value;
		if (end == -1) value = text.substring(start);
		else value = text.substring(start, end);

		return value.trim();
	}

	/**
	 * Returns an attribute in result.
	 * expected format "!title = ???".
	 * @param title the name of the attribute
	 * @return the value of the attribute
	 */
	public static String getAttribute(String title, String text){
		String result = getSection(title, text);
		if (result == null) return null;
		else return result.substring(result.indexOf(SEPERATOR)+1).trim();
	}
	
	public static String buildSection(String title, String text){
		return DELIMITER+title+"\n"+text;
	}
	public static String buildAttribute(String title, String value){
		return DELIMITER+title+SEPERATOR+value;
	}
}
