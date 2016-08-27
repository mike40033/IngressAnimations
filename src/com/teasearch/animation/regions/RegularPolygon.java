package com.teasearch.animation.regions;


public class RegularPolygon extends RegionOfControl{
	
	private double theta;
	private int n;
	private double cx;
	private double cy;
	private double startingRadius;
	private double rotationSpeed;

	public RegularPolygon(int n, double cx, double cy, double theta, double startingRadius){
		this(n,cx,cy,theta,startingRadius,0);
	}
	public RegularPolygon(int n, double cx, double cy, double theta, double startingRadius, double rotationSpeed){
		this.n = n;
		this.cx = cx;
		this.cy = cy;
		this.theta = theta;
		this.startingRadius = startingRadius;
		this.rotationSpeed = rotationSpeed;
	}

	@Override
	public double[][] getPolygon(double radius) {
		radius += startingRadius;
		if (radius < 0) radius = 0;
		double[] x = new double[n];
		double[] y= new double[n];
		for (int i=0;i<n; i++) {
			x[i] = radius*Math.cos(theta + 2*Math.PI*i/n+rotationSpeed*radius)+cx;
			y[i] = radius*Math.sin(theta + 2*Math.PI*i/n+rotationSpeed*radius)+cy;
		}
		return new double[][] {x,y};
	}

}
