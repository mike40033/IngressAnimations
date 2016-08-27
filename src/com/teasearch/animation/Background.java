package com.teasearch.animation;

import java.awt.Color;
import java.awt.Graphics;

public class Background extends AnimableItem{

	private Color start;
	private Color end;

	public Background(Color start, Color end, double duration) {
		super(duration);
		this.start = start;
		this.end = end;
	}
	
	@Override
	protected void drawIntermediate(Graphics gr, Scale scale, double t) {
		fill(gr, scale, t);
	}

	private void fill(Graphics gr, Scale scale, double t) {
		gr.setColor(getColor(t));
		gr.fillRect(0, 0, scale.getWidth(), scale.getHeight());
	}

	private Color getColor(double t) {
		float r = interpolate(start.getRed(), end.getRed(), t);
		float g = interpolate(start.getGreen(), end.getGreen(), t);
		float b = interpolate(start.getBlue(), end.getBlue(), t);
		return new Color(r,g,b);
	}

	private float interpolate(int s, int e, double u) {
		float t = (float)u;
		return (s/255f * (1-t) + e/255f*t);
	}

	@Override
	protected void drawInitial(Graphics gr, Scale scale) {
		fill(gr,scale,0);
	}

	@Override
	protected void drawFinal(Graphics gr, Scale scale) {
		fill(gr,scale,1);
	}
		
}
