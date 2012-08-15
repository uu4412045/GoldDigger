package com.golddigger.view.renderer;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.golddigger.model.Game;
import com.golddigger.model.Unit;

public interface FieldRenderer {
	public void render(Graphics graphics, Rectangle bounds);
}
