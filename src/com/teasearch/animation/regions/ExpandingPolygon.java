package com.teasearch.animation.regions;

public class ExpandingPolygon extends RegionOfControl {
	private double[] centre;
	private double[][] v;
	
	
	
	public ExpandingPolygon(double[] centre, double[]... v) {
		this.centre = centre;
		this.v = v;
	}


	@Override
	public double[][] getPolygon(double lambda) {
		double[] x = new double[v.length];
		double[] y= new double[v.length];
		for (int i=0;i<v.length; i++) {
			x[i] = centre[0] + lambda*v[i][0];
			y[i] = centre[1] + lambda*v[i][1];
		}
		return new double[][] {x,y};
	}
}
