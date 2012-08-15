package com.golddigger.templates.generators;

import java.io.File;

import com.golddigger.utils.FieldFileTemplateGenerator;

public class CompetitionTemplateGenerator extends FieldFileTemplateGenerator {
	public static final String DEFAULT_PATH = "src/main/resources/fields/";
	public static final String CUSTOM_PATH = "../fields";
	
	public CompetitionTemplateGenerator() {
		File dir = new File(CUSTOM_PATH);
		if (!dir.exists()) {
			dir = new File(DEFAULT_PATH);
		}
		System.err.println("Res File"+dir.getAbsolutePath());
		this.setDirectory(dir);
	}

	

}
