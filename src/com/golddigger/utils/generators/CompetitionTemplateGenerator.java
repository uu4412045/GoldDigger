package com.golddigger.utils.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.List;

import com.golddigger.core.GameTemplate;
import com.golddigger.utils.JSONTemplateParser;

public class CompetitionTemplateGenerator implements TemplateGenerator {
	public static final String DEFAULT_FIELD_DIR = "../fields/";
	public static final String PACKED_FIELD_DIR  = "./fields/";
	List<GameTemplate> templates;
	
	public CompetitionTemplateGenerator(){
		File dir = new File(DEFAULT_FIELD_DIR);
		if (!dir.exists() || !dir.isDirectory()) {
			dir = new File(PACKED_FIELD_DIR);
		}
		File[] fields = dir.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File file, String name) {
				return name.endsWith(".jgame");
			}
		});
		for (File field : fields){
			try {
				templates.add(JSONTemplateParser.parse(new FileReader(field)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public GameTemplate next() {
		
		return null;
	}

}
