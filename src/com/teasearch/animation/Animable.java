package com.teasearch.animation;

import java.awt.Graphics;

public interface Animable {
	
	public double getDuration();
	
	public void draw(Graphics gr, ScaleAnimation scale, double t);
}
