package com.golddigger.templates.generators;

import com.golddigger.templates.GameTemplate;


/**
 * 
 * @author Brett Wandel
 *
 */
public interface TemplateGenerator {
	/**
	 * Creates a ordered list of {@link GameTemplate} templates, to be loaded into a server.
	 * Each call to this MUST create new GameTemplate instances.
	 * @return list of GameTemplates
	 * @see GameTemplate
	 */
	public GameTemplate[] generate();
}
