package com.golddigger;
import com.golddigger.templates.GameTemplate;
import com.golddigger.templates.generators.CompetitionTemplateGenerator;


public class CompetitionServer extends GenericServer {
	private static final int SECONDS = 1000;
	private static final int MINUTES = 60*SECONDS;

	String[] players;
	
	public CompetitionServer(){
		super(5*MINUTES, false);
		
		for (GameTemplate template : new CompetitionTemplateGenerator().generate()){
			this.addTemplate(template);
		}
		
		players = new String[]{"test1,secret1","test2,secret2"};
		
		for (String player : players){
			this.addPlayer(player.split(",")[0].trim(), player.split(",")[1].trim());
		}
	}
	
	public static void main(String[] args){
		new CompetitionServer();
	}
}
