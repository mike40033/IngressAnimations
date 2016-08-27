package com.teasearch.animation.regions;

import com.teasearch.animation.fibotribo.OldGoldenSpiral;


public class FibonacciRectangles extends RegionOfControl{

	@Override
	public double[][] getPolygon(double lambda) {
		if (lambda < 1e-12) lambda = 1e-12;
		double[] x = new double[4];
		double[] y = new double[4];
		double t = Math.log(lambda);
		int whole = (int)(Math.floor(t));
		double frac = t - whole;
		int index = whole%4;
		int power = whole / 4;
		double p = OldGoldenSpiral.p;
		double[][][] base = OldGoldenSpiral.base;
		double[][] pp = base[index];
		double[][] qq = base[index+1];
		double scale = Math.pow(3*p+2,power);
		for (int i=0; i<4; i++) {
			x[i] = (pp[i][0]*(1-frac) + qq[i][0]*frac)*scale;
			y[i] = (pp[i][1]*(1-frac) + qq[i][1]*frac)*scale;
		}
		return new double[][] {x,y};
	}

}
