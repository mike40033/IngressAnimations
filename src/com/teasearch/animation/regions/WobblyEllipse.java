package com.teasearch.animation.regions;


public class WobblyEllipse extends RegionOfControl {

	@Override
	public double[][] getPolygon(double radius) {
		int n = 360;
		if (radius < 1e-16) radius = 1e-16;
		double multiplier = 6*Math.PI;
		double logRadius = Math.log(radius)*multiplier;
		double logXR = logRadius + Math.sin(logRadius);
		double logYR = logRadius - Math.sin(logRadius);
		double xradius = Math.exp(logXR/multiplier);
		double yradius = Math.exp(logYR/multiplier);
		double[] x = new double[n];
		double[] y= new double[n];
		for (int i=0;i<n; i++) {
			x[i] = xradius*Math.cos(multiplier * i/n);
			y[i] = yradius*Math.sin(multiplier * i/n);
		}
		return new double[][] {x,y};
	}

}
