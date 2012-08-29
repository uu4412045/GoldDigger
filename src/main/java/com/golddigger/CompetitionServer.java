package com.golddigger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.golddigger.templates.GameTemplate;
import com.golddigger.utils.FieldFileLoader;

/**
 * <p>CompetitionServer is the server that is run when the jar file is executed.
 * It loads all the "x.field" files from either the custom fields folder, or the 
 * jars resources. It will also load the players from the players.txt file.
 * </p>
 * <p>To load custom games, create a folder called "fields" in the same folder as the
 * jar file. Add the fields, with the name starting at 1 and incrementing by one for
 * each successive "level".
 * </p>
 * <p>To load players from a list, create a "players.txt" file in the same folder as the
 * jar file. each player should be on their own line with a comma between the name and
 * password.... eg:
 * <pre>
 * brett,secret
 * someone,verysecret
 * </pre></p>
 * 
 * @author Brett Wandel
 *
 */
public class CompetitionServer extends GenericServer {
	private static final int SECONDS = 1000;
	private static final int MINUTES = 60*SECONDS;

	/**
	 * Creates a new Competition server which consists of a 5minute delay and
	 * GUIs. Loads all the templates found by {@link FieldFileLoader}.
	 */
	public CompetitionServer(){
		super(5*MINUTES, Mode.GUI);
		
		for (GameTemplate template : FieldFileLoader.load()){
			this.addTemplate(template);
		}
		
		String[] players = loadPlayers();
		if (players == null) players = new String[]{"test1,secret1","test2,secret2"};
		
		for (String player : players){
			this.addPlayer(player.split(",")[0].trim(), player.split(",")[1].trim());
		}
	}
	
	/**
	 * Returns the players details from the custom players list.
	 * @return the list of comma seperated name and password
	 */
	private String[] loadPlayers() {
		File file = new File("./players.txt");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null, output="";
			while ((line = reader.readLine()) != null){
				output += line + "\n";
			}
			return output.split("\n");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args){
		new CompetitionServer();
	}
}
