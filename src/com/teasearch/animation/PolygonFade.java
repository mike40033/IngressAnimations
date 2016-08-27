package com.teasearch.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class PolygonFade extends AnimableItem {

	private Color c;
	private double[] xs;
	private double[] ys;

	public PolygonFade(Color c, double[] xs, double[] ys, double duration) {
		super(duration);
		this.c = c;
		this.xs = xs;
		this.ys = ys;
	}

	@Override
	protected void drawIntermediate(Graphics gr, Scale scale, double u) {
		gr.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(u*64)));
		int[] ix = new int[xs.length];
		int[] iy = new int[xs.length];
		for (int i=0; i<ix.length; i++) {
			int[] ixy = scale.toGraphics(xs[i], ys[i]);
			ix[i] = ixy[0];
			iy[i] = ixy[1];
		}
		Polygon p = new Polygon(ix, iy, ix.length);
		gr.fillPolygon(p);
	}

	@Override
	protected void drawInitial(Graphics gr, Scale scale) {

	}

	@Override
	protected void drawFinal(Graphics gr, Scale scale) {
		drawIntermediate(gr, scale, 1);
	}

}
