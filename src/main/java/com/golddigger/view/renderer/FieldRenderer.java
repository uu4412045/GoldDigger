package com.golddigger.view.renderer;

import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * FieldRenderer uses a Swing Components Graphic object to draw/render
 * a field/map centred on a particular unit
 *
 * @author Brett Wandel
 */
public interface FieldRenderer {
	/**
	 * Render the map within the viewport.
	 * @param graphics The Graphics object to draw on
	 * @param bounds The "viewport" centered on the unit
	 */
	public void render(Graphics graphics, Rectangle bounds);
}
