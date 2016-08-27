package com.teasearch.animation.fibotribo;

import java.awt.Color;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.teasearch.animation.AxisAlignedScale;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.clips.Animate;
import com.teasearch.utils.GeomUtils;
import com.teasearch.utils.MathUtils;

public class TriboSpiral  extends SpiralShape{

	private int n;

	public TriboSpiral(double[] startSquare, int startSquareIndex,
			int endSquareIndex, double timePerSquare, int numberOfColours) {
		super(startSquare, startSquareIndex, endSquareIndex, timePerSquare);
		this.n = numberOfColours;
	}

	@Override
	public List<ColouredPolygon> getStartingPolygons(double[] shapeVector, int index) {
		List<ColouredPolygon> rtn = new LinkedList<>();
		Color c = getColor(index*3-1);
		double[] P = {shapeVector[0], shapeVector[1]};
		double[] Q = {shapeVector[2], shapeVector[3]};
		double pq = GeomUtils.getDistance(P, Q);
		double qr = shapeVector[4];
		double rs = shapeVector[5];
		double[] v0 = {(Q[0]-P[0])/pq, (Q[1]-P[1])/pq};
		double[] v1 = {(v0[0]+SQRT_3*v0[1])/2, (-SQRT_3*v0[0]+v0[1])/2};
		double[] v2 = {(v1[0]+SQRT_3*v1[1])/2, (-SQRT_3*v1[0]+v1[1])/2};
		double[] R = vectorShift(Q, qr, v1);
		double[] S = vectorShift(R, rs, v2);
		double[] U = vectorShift(P, rs, v2);
		double[] T = vectorShift(U, qr, v1);
		rtn.add(new ColouredPolygon(new double[] {P[0],Q[0],R[0],S[0],T[0],U[0]} , new double[] {P[1],Q[1],R[1],S[1],T[1],U[1]},c));
		return rtn;
	}

	private static final double SQRT_3 = Math.sqrt(3);
	
	@Override
	protected double[] nextShape(double[] shapeVector) {
		double[] P = {shapeVector[0], shapeVector[1]};
		double[] Q = {shapeVector[2], shapeVector[3]};
		double pq = GeomUtils.getDistance(P, Q);
		double qr = shapeVector[4];
		double rs = shapeVector[5];
		double[] v0 = {(Q[0]-P[0])/pq, (Q[1]-P[1])/pq};
		double[] v1 = {(v0[0]+SQRT_3*v0[1])/2, (-SQRT_3*v0[0]+v0[1])/2};
		double[] R = {Q[0]+qr*v1[0], Q[1]+qr*v1[1]};
		return new double[] {Q[0],Q[1],R[0],R[1],rs, pq+qr+rs};
	}

	@Override
	protected List<ColouredPolygon> getPartialPolygons(double[] shapeVector, double frac, int index) {
		double[] P = {shapeVector[0], shapeVector[1]};
		double[] Q = {shapeVector[2], shapeVector[3]};
		double pq = GeomUtils.getDistance(P, Q);
		double qr = shapeVector[4];
		double rs = shapeVector[5];
		double[] v0 = {(Q[0]-P[0])/pq, (Q[1]-P[1])/pq};
		double[] v1 = {(v0[0]+SQRT_3*v0[1])/2, (-SQRT_3*v0[0]+v0[1])/2};
		double[] v2 = {(v1[0]+SQRT_3*v1[1])/2, (-SQRT_3*v1[0]+v1[1])/2};
		double[] R = vectorShift(Q, qr, v1);
		double[] S = vectorShift(R, rs, v2);
		double[] U = vectorShift(P, rs, v2);
		double[] T = vectorShift(U, qr, v1);
		double[] W = vectorShift(U, qr, v2);
		double[] V = vectorShift(P, -rs, v0);
		double[] Z = vectorShift(V, -qr, v0);
		double[] X = vectorShift(W, -rs, v0);
		double[] Y = vectorShift(Z, rs, v2);
		double frac1 = Math.min(2*frac,1);
		double frac2 = Math.max(2*frac-1, 0);
		double frac1a = frac1*frac1;
		double frac1b = 2*frac1-frac1a;
		List<ColouredPolygon> rtn = new LinkedList<>();
		// first, two triangles...
		// first triangle is U, T, and grows from T to W.
		Color c1 = getColor(index*3);
		double[] T1a = U;
		double[] T2a = T;
		double[] T3a = pointShift(T,frac1b,W);
		rtn.add(new ColouredPolygon(new double[] {T1a[0], T2a[0], T3a[0]}, new double[] {T1a[1], T2a[1], T3a[1]} , c1));
		// first triangle is U, P, and grows from P to V.
		Color c2 = getColor(index*3+1);
		double[] T1b = U;
		double[] T2b = P;
		double[] T3b = pointShift(P,frac1a,V);
		rtn.add(new ColouredPolygon(new double[] {T1b[0], T2b[0], T3b[0]}, new double[] {T1b[1], T2b[1], T3b[1]} , c2));
		if (frac2 <= 0) return rtn;
		// finally, the hexagon, made of two quadrangles
		double frac2a = frac2*frac2;
		double frac2b = 2*frac2-frac2a;
		double[] H1a = U;
		double[] H2a = W;
		double[] H3a = pointShift(W,frac2b,X);
		double[] H4a = pointShift(W,frac2a,Y);
		Color c3 = getColor(index*3+2);
		rtn.add(new ColouredPolygon(new double[] {H1a[0], H2a[0], H3a[0], H4a[0]}, new double[] {H1a[1], H2a[1], H3a[1], H4a[1]} , c3));
		double[] H1b = U;
		double[] H2b = V;
		double[] H3b = pointShift(V,frac2b,Z);
		double[] H4b = pointShift(V,frac2a,Y);
		rtn.add(new ColouredPolygon(new double[] {H1b[0], H2b[0], H3b[0], H4b[0]}, new double[] {H1b[1], H2b[1], H3b[1], H4b[1]} , c3));
		// we'll also need a bit of spiral. Later.
		return rtn;
	}

	private double[] pointShift(double[] A, double frac, double[] B) {
		return new double[] {A[0]*(1-frac)+B[0]*frac, A[1]*(1-frac)+B[1]*frac};
	}

	private Color getColor(int i) {
		return OldGoldenSpiral.generatePastels(n)[MathUtils.mod(i, n)];
	}

	private double[] vectorShift(double[] Q, double qr, double[] v1) {
		return new double[] {Q[0]+qr*v1[0], Q[1]+qr*v1[1]};
	}
	
	public static void main(String[] args) {
		final int width = 1200;
		final int height = 1200;
		ScaleAnimation zoomingScale = new ScaleAnimation() {
			@Override
			public Scale getScale(double t) {
				return new AxisAlignedScale(width, height, 0.0, 0.0, Math.pow(MathUtils.TRIBO, t)*2.7);
			}

			@Override
			public int getWidth() {
				return width;
			}

			@Override
			public int getHeight() {
				return height;
			}
		};
		File folder = new File("/home/hartleym/Desktop/Animation/Tribo/");
		String filePrefix = "tribo";
		double frameRate = 25;
		boolean scaleOutput = false;
		double[] square = {0,0,-Math.pow(MathUtils.TRIBO,-17),0,Math.pow(MathUtils.TRIBO,-16),Math.pow(MathUtils.TRIBO,-15)};
		TriboSpiral animation = new TriboSpiral(square,-16, 6, 1.0, 18);
		new Animate(animation, zoomingScale, folder, filePrefix, frameRate, scaleOutput).makeClip();
	}

}
