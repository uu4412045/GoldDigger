package com.golddigger.utils;

import com.golddigger.templates.CustomizableGameTemplate;
import com.golddigger.templates.GameTemplate;

public class LegacyTemplateParser {
	public static final String DELIMITER = "!", SEPERATOR = "=";

	public final static String TILES = "field-tiles",
			COSTS = "cost-per-type",
			LINE_OF_SIGHT = "line-of-sight",
			NUMBER_OF_SIDES   = "number-of-sides",
			PLUGINS = "plugins",
			CANNON = "enable-cannons",
			DIS_TELEPORTS = "dis-teleport-mappings",
			OCCLUSION = "enable-occlusion",
			MULTIPLAYER = "multiplayer";

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
			int numberOfSides = getNumberOfSides(text);
			if (numberOfSides != 4) template.setNumberOfSides(numberOfSides);
			
			processMultiplayerTimes(template, text);
			
			if (getSection(CANNON, text) != null) template.enableCannons(true);
			if (getSection(OCCLUSION, text) != null) template.enableOcclusion(true);
			
			template.setDTeleportTiles(parseDTeleports(text));
		} else {
			template.setMap(text.trim());
		}
		return template;
	}
	
	private static int getNumberOfSides(String text) {
		String value = getAttribute(NUMBER_OF_SIDES, text);
		if (value == null) return 4;
		else return Integer.parseInt(value);
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
	 * Returns an array of Strings of the form "srcX,srcY-dstX,dstY". In the field
	 * file, teleportation mappings are expected as: !dis-teleport-mappings =
	 * 2,3-4,5\n10,11-12,13\n... such that source tile (2,3) teleports to
	 * destination (4,5) etc
	 */
	public static String[] parseDTeleports(String text) {
		String section = getSection(DIS_TELEPORTS, text);
		if (section == null) {
			return null;
		}
		String[] dTeleports = section.split("\n");
		return dTeleports;
		
	}
	
	/**
	 * Extract the multiplayer times and set up the template.
	 * @param template The template to be modified
	 * @param text The text from the field file to be parsed
	 * @return The modified template
	 */
	private static void processMultiplayerTimes(CustomizableGameTemplate template, String text) {
		String lines = getSection(MULTIPLAYER, text);
		int start = -1, duration = -1, end = -1; 
		
		if (lines != null) {
			for (String line : lines.split("\n")){
				String[] parts = line.split(SEPERATOR);
				if (parts.length != 2) continue;
				if (parts[0] == null || parts[1] == null) continue;
				int value = -1;
				
				try {
					value = Integer.parseInt(parts[1]);
				} catch (NumberFormatException e){
					System.out.println("Failed to parse value "+line);
				}
				
				if (parts[0].equalsIgnoreCase("start")){
					start = value;
				} else if (parts[0].equalsIgnoreCase("duration")){
					duration = value;
				} else if (parts[0].equalsIgnoreCase("end")){
					end = value;
				} else {
					System.out.println("Failed to parse title "+line);
				}
			}
			
			template.setMultiplayer(start, duration,end);
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
		return DELIMITER+title+"\n"+text+"\n";
	}
	public static String buildAttribute(String title, String value){
		return DELIMITER+title+SEPERATOR+value+"\n";
	}
}
