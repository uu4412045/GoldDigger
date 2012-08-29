package com.golddigger.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.golddigger.templates.GameTemplate;
/**
 * <p>This is the main class that does the loading of the field files.
 * Uses LegacyTemplateParser to convert each file into a GameTemplate</p>
 * 
 * @author Brett Wandel
 * @see LegacyTemplateParser
 * @see GameTemplate
 */
public class FieldFileLoader {
	public static final String DEFAULT_PATH = "/fields/";
	public static final String CUSTOM_PATH = "fields/";

	/**
	 * Load all the .field files and convert them into GameTemplates
	 * @return the templates ready to be added to the server
	 * @see LegacyTemplateParser
	 */
	public static GameTemplate[] load() {
		List<GameTemplate> templates = new ArrayList<GameTemplate>();
		File dir = new File(CUSTOM_PATH);
		System.out.println("checking folder @ "+dir.getAbsolutePath());
		boolean loadCustoms = (dir.exists() && dir.isDirectory());
		System.out.println("found custom folder? "+loadCustoms);
		int n = 1;
		InputStream in = null;
		while ((in = getStream(n, loadCustoms)) != null){
			System.out.println("loaded field "+n+", converting");
			try {
				String text = getText(in);
				GameTemplate template = LegacyTemplateParser.parse(text);
				templates.add(template);
			} catch (IOException e){
				e.printStackTrace();
				continue;
			}
			n++;
		}
		return templates.toArray(new GameTemplate[]{});
	}
	
	/**
	 * get the InputStream for the particular field file
	 * @param n the "n.field" file
	 * @param custom are we loading custom field files?
	 * @return null if no file was found
	 */
	private static InputStream getStream(int n, boolean custom){
		if (custom){
			try {
				return new FileInputStream(CUSTOM_PATH+n+".field");
			} catch (FileNotFoundException e) {
				return null;
			}
		} else {
		return FieldFileLoader.class.getResourceAsStream(DEFAULT_PATH+n+".field");
		}
	}
	
	/**
	 * Used to get all the text from a field file
	 * @param in The InputStream associated with the field file
	 * @return The contents of the field file
	 * @throws IOException
	 */
	private static String getText(InputStream in) throws IOException {
		String contents = "";
		BufferedReader buff =  new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = buff.readLine()) != null) contents += line+"\n";
		buff.close();
		return contents;
	}
}
