package com.teasearch.animation.regions;


public class SpinningRegularPolygon extends RegionOfControl{
	
	private double theta;
	private int n;
	private double cx;
	private double cy;
	private double startingRadius;
	private double rotationSpeed;

	public SpinningRegularPolygon(int n, double cx, double cy, double theta, double startingRadius){
		this.n = n;
		this.cx = cx;
		this.cy = cy;
		this.theta = theta;
		this.startingRadius = startingRadius;
	}

	@Override
	public double[][] getPolygon(double radius) {
		radius += startingRadius;
		if (radius < 1e-12) radius = 1e-12;
		double[] x = new double[n];
		double[] y= new double[n];
		double phi = 2*Math.PI/n;
		double factor = Math.sin(phi)/(Math.cos(phi)-1);
		for (int i=0;i<n; i++) {
			double logR = Math.log(radius);
			x[i] = radius*Math.cos(theta + phi*i+logR*factor)+cx;
			y[i] = radius*Math.sin(theta + phi*i+logR*factor)+cy;
		}
		return new double[][] {x,y};
	}

}
