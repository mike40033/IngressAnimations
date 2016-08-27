package com.teasearch.animation.regions;


public class SweepingArc extends RegionOfControl {

	@Override
	public double[][] getPolygon(double theta) {
		if (theta < 0) theta = 0;
		if (theta > Math.PI/2) theta = Math.PI/2;
		double[] x = {-650,-650+2500*Math.cos(theta),20000};
		double[] y = {-550,-550+2500*Math.sin(theta),-800};
		return new double[][] {x,y};
	}

}
