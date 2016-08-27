package com.teasearch.animation.regions;

import java.awt.Color;
import java.awt.Graphics;
import java.util.TreeMap;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.utils.GeomUtils;


/** represents a growing region of control. */
public abstract class RegionOfControl {
	
	/** the contract here is: if lambda1 > lambda2, then getPolygon(lambda1) should contain getPolygon(lambda2) 
	 * Also, getPolygon(0) should not contain any point of interest.
	 * Also, there must exist a (not too big) lambda such that getPolygon(lambda) contains all points of interest 
	 * Also, the polygon should always be convex. 
	 * polygon.length==2.*/
	public abstract double[][] getPolygon(double lambda);
	
	public double getLambda(double[] pt) {
		double a = 0;
		double[][] pa = getPolygon(a);
		if (GeomUtils.pointInConvexPolygon(pt, pa)) {
			throw new UnsupportedOperationException("not implemented: points inside an initial RegionOfControl.");
		}
		double b = 1;
		double[][] pb = getPolygon(b);
		while (!GeomUtils.pointInConvexPolygon(pt, pb)) {
			a = b;
			pa = pb;
			b *= 2;
			pb = getPolygon(b);
		}
		// pb contains pt but not pa.
		while (b-a > 1e-6) {
			double c = (a+b)/2;
			double[][] pc = getPolygon(c);
			if (GeomUtils.pointInConvexPolygon(pt, pc)) {
				b = c;
				pb = pc;
			} else {
				a = c;
				pa = pc;
			}
		}
		return (a+b)/2;
	}
	
	public Animable animateRegion(final TreeMap<Double, Double> timeToLambdaMap, final Color color) {
		return new Animable() {
			
			@Override
			public double getDuration() {
				return timeToLambdaMap.lastKey() - timeToLambdaMap.firstKey();
			}
			
			@Override
			public void draw(Graphics gr, ScaleAnimation scale, double t) {
				if (t < timeToLambdaMap.firstKey()) {
					t = timeToLambdaMap.firstKey();
				}
				if (t > timeToLambdaMap.lastKey()) {
					t = timeToLambdaMap.lastKey();
				}
				if (timeToLambdaMap.containsKey(t)) {
					draw(gr,scale.getScale(t),getPolygon(timeToLambdaMap.get(t)));
					return;
				}
				double t1 = timeToLambdaMap.headMap(t).lastKey();
				double t2 = timeToLambdaMap.tailMap(t).firstKey();
				double lam1 = timeToLambdaMap.get(t1);
				double lam2 = timeToLambdaMap.get(t2);
				double lambda = (lam2 * (t-t1) + lam1 * (t2-t))/(t2-t1);
				draw(gr,scale.getScale(t),getPolygon(lambda));
			}

			private void draw(Graphics gr, Scale scale, double[][] polygon) {
				if (polygon == null) return;
				int n = polygon[0].length;
				int[] ix = new int[n];
				int[] iy = new int[n];
				for (int i=0; i<n; i++) {
					int[] ixy = scale.toGraphics(polygon[0][i], polygon[1][i]);
					ix[i] = ixy[0];
					iy[i] = ixy[1];
				}
				gr.setColor(color);
				gr.fillPolygon(ix, iy, n);
			}
		};
	}
	
}
