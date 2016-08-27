package com.teasearch.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Line extends AnimableItem {

	private double x1;
	private double y1;
	private double x2;
	private double y2;
	private Color c;

	public Line(Color c, Stroke s, double x1, double y1, double x2, double y2, double duration) {
		super(duration);
		this.c = c;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	protected void drawIntermediate(Graphics gr, Scale scale, double t) {
		draw(gr, scale, x1,y1,x2*t+x1*(1-t),y2*t+y1*(1-t));
	}

	@Override
	protected void drawInitial(Graphics gr, Scale scale) {
	}

	@Override
	protected void drawFinal(Graphics gr, Scale scale) {
		draw(gr, scale, x1,y1,x2,y2);
	}

	private void draw(Graphics gr, Scale scale, double x1, double y1, double x2, double y2) {
		int[] start = scale.toGraphics(x1, y1);
		int[] end = scale.toGraphics(x2, y2);
		gr.setColor(c);
		((Graphics2D)gr).setStroke(new BasicStroke(scale.getWidth()/300f));
		gr.drawLine(start[0], start[1], end[0], end[1]);
	}

}
