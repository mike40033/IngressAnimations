package com.teasearch.animation.regions;


public class TransformedRegion extends RegionOfControl {

	private double[] translation;
	private double[][] linearTransform;
	private RegionOfControl delegate;

	public TransformedRegion(RegionOfControl delegate, double[][] linearTransform, double[] translation) {
		this.delegate = delegate;
		this.linearTransform = linearTransform;
		this.translation = translation;
	}
	
	@Override
	public double[][] getPolygon(double lambda) {
		double[][] rtn = delegate.getPolygon(lambda);
		for (int i=0; i<rtn[0].length; i++) {
			double x1 = rtn[0][i];
			double y1 = rtn[1][i];
			double x2 = linearTransform[0][0]*x1 + linearTransform[0][1]*y1 + translation[0];
			double y2 = linearTransform[1][0]*x1 + linearTransform[1][1]*y1 + translation[1];
			rtn[0][i] = x1;
			rtn[1][i] = y1;
		}
		return rtn;
	}

}
