package com.teasearch.animation.ingress.portals;

import java.awt.Color;

import com.teasearch.animation.ingress.stats.Corner;

public class Portal {
	private double x;
	private double y;
	private Color initialColor;
	private static int nextID=0;
	private int id = nextID++;
	public Portal(double x, double y, Color initialColor) {
		this.x = x;
		this.y = y;
		this.initialColor = initialColor;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public Color getInitialColor() {
		return initialColor;
	}
	
	@Override
	public int hashCode() {
		return (int) (3333*Double.doubleToLongBits(x)+40033*Double.doubleToLongBits(y)+initialColor.hashCode()*1729);
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Portal)) return false;
		Portal other = (Portal)o;
		return (this.x == other.x) && (this.y == other.y) && (this.initialColor == other.initialColor);
	}

	public double[] getPt() {
		return new double[] {x,y};
	}
	
	public String toString() {
		return "P"+id;
	}

	public void setLabel(double time, String text, Corner corner, Color color) {
		// TODO Auto-generated method stub
		
	}
	
}
