package com.teasearch.animation.regions;


public class ExpandingRectangle extends ExpandingPolygon{
	
	/** actually, this can be an arbitrary parallelogram */
	public ExpandingRectangle(double[] centre, double[] v1, double[] v2) {
		super(centre, v1, v2, new double[] {-v1[0],-v1[1]}, new double[] {-v2[0],-v2[1]});
	}

	
	
}
