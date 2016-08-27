package com.teasearch.animation.fibotribo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;

public abstract class SpiralShape implements Animable {
	private double[] startSquare;
	private int endSquareIndex;
	private double timePerSquare;
	private int startSquareIndex;
	private Color background;
	
	public SpiralShape(double[] startSquare, int startSquareIndex, int endSquareIndex, double timePerSquare) {
		this(startSquare, startSquareIndex, endSquareIndex, timePerSquare, Color.white);
	}
	public SpiralShape(double[] startSquare, int startSquareIndex, int endSquareIndex, double timePerSquare, Color background) {
		this.startSquare = startSquare;
		this.startSquareIndex = startSquareIndex;
		this.endSquareIndex = endSquareIndex;
		this.timePerSquare = timePerSquare;
		this.background = background;
	}

	@Override
	public double getDuration() {
		return endSquareIndex*timePerSquare;
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		gr.setColor(background);
		gr.fillRect(0, 0, scale.getWidth(), scale.getHeight());
		draw(gr, scale.getScale(t), getStartingPolygons(startSquare, startSquareIndex));
		draw(gr, scale, t, startSquareIndex, startSquare);
	}

	public abstract List<ColouredPolygon> getStartingPolygons(double[] vector, int index);

	public void draw(Graphics gr, ScaleAnimation scale, double t, int index, double[] square) {
		if (index > t) return;
		if (index+1 <= t) {
			draw(gr, scale.getScale(t), getPolygons(square, index));
		} else {
			draw(gr, scale.getScale(t), getPartialPolygons(square, t-index, index));
		}
		draw(gr, scale, t, index+1, nextShape(square));
	}

	public static class ColouredPolygon {
		public double[] x;
		public double[] y;
		public Color c;
		
		static int maxX = Integer.MIN_VALUE;
		static int maxY = Integer.MIN_VALUE;
		static int minX = Integer.MAX_VALUE;
		static int minY = Integer.MAX_VALUE;
		
		public ColouredPolygon(double[] x, double[] y, Color c) {
			this.x = x;
			this.y = y;
			this.c = c;
		}

		public void draw(Graphics gr, Scale scale) {
			gr.setColor(c);
			int n = x.length;
			int[] ix = new int[n];
			int[] iy = new int[n];
			for (int i=0; i<n; i++) {
				int[] ixy = scale.toGraphics(x[i], y[i]);
				ix[i] = ixy[0];
				iy[i] = ixy[1];
				boolean print = false;
				if (ix[i] < minX) {
					minX = ix[i];
					print = true;
				}
				if (ix[i] > maxX) {
					maxX = ix[i];
					print = true;
				}
				if (iy[i] < minY) {
					minY = iy[i];
					print = true;
				}
				if (iy[i] > maxY) {
					maxY = iy[i];
					print = true;
				}
				if (print) {
					System.out.println("range x:"+minX+"-"+maxX+", y:"+minY+"-"+maxY+" @ SpiralShape.ColouredPolygon.draw()");
				}
			}
			gr.fillPolygon(ix, iy, n);
		}
		
	}
	
	protected abstract double[] nextShape(double[] shapeVector);

	protected abstract List<ColouredPolygon> getPartialPolygons(double[] square, double frac, int index);

	private List<ColouredPolygon> getPolygons(double[] square, int index) {
		return getPartialPolygons(square, 1.0, index);
	}

	private void draw(Graphics gr, Scale scale, List<ColouredPolygon> polygons) {
		for (ColouredPolygon p : polygons) {
			p.draw(gr,scale);
		}
	}
}
