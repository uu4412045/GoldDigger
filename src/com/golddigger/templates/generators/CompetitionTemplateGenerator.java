package com.golddigger.templates.generators;

import java.io.File;

import com.golddigger.utils.FieldFileTemplateGenerator;

public class CompetitionTemplateGenerator extends FieldFileTemplateGenerator {

	public CompetitionTemplateGenerator() {
		File dir = new File("../fields");
		if (!dir.exists()) {
			dir = new File("res/fields");
		}
		
		this.setDirectory(dir);
	}

	

}