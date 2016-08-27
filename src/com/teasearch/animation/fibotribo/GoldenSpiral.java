package com.teasearch.animation.fibotribo;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.teasearch.animation.AxisAlignedScale;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.clips.Animate;
import com.teasearch.utils.GeomUtils;
import com.teasearch.utils.MathUtils;

public class GoldenSpiral extends SpiralShape{

	private int numberOfColors;

	public GoldenSpiral(double[] startSquare, int startSquareIndex,	int endSquareIndex, double timePerSquare, int numberOfColors) {
		super(startSquare, startSquareIndex, endSquareIndex, timePerSquare);
		this.numberOfColors = numberOfColors;
	}

	/** shapeVector has 12 coordinates, being the x/y coordinates of the square, and the two other corners of the previous rectange 
	 * 
	 *    0-------2---4
	 *    !       !   !
	 *    !       !   ! 
	 *    !       !   !
	 *    1-------3---5
	 *    
	 *    we don't need 4 and 5 for drawing, but we do need them to calculate the next square
	 *     
	 * */
	
	
	
	@Override
	protected double[] nextShape(double[] prev) {
		double[] next = new double[12];
		next[0] = prev[2] - prev[3] + prev[11];
		next[1] = prev[3] + prev[2] - prev[10];

		next[2] = prev[10] - prev[3] + prev[11];
		next[3] = prev[11] + prev[2] - prev[10];

		next[4] = prev[2]; 
		next[5] = prev[3];

		next[6] = prev[10];
		next[7] = prev[11];

		next[8] = prev[0];
		next[9] = prev[1];

		next[10] = prev[8];
		next[11] = prev[9];
		return next;
	}

	@Override
	protected List<ColouredPolygon> getPartialPolygons(double[] vec, double frac, int index) {
		// two polygons: a white spiral segment, and the square 0132 scaled by t. 
		List<ColouredPolygon> rtn = new LinkedList<>();
		// first, build the spiral segment.
		double[] p0 = {vec[0],vec[1]};
		double[] p1 = {vec[2],vec[3]};
		double[] p2 = {vec[4],vec[5]};
		double[] p3 = {vec[6],vec[7]};
		double[] p4 = {vec[8],vec[9]};
		double radius1 = GeomUtils.getDistance(p2, p3);
		double radius2a = (radius1 - GeomUtils.getDistance(p2, p4)*0.1)/radius1;
		double radius2b = (radius1 - GeomUtils.getDistance(p2, p3)*0.1)/radius1;
		double[] va = {p2[0]-p3[0], p2[1]-p3[1]};
		double[] vb = {p1[0]-p3[0], p1[1]-p3[1]};
		// now, build 90 points on each curve:
		double[] x = new double[182];
		double[] y = new double[182];
		double maxSin = -1; 
		for (int i=0; i<=90; i++) {
			double cos = Math.cos(i*Math.PI/90/2*frac);
			double sin = Math.sin(i*Math.PI/90/2*frac);
			maxSin = Math.max(maxSin, sin);
			x[i] = p3[0] + va[0]*cos+vb[0]*sin;
			y[i] = p3[1] + va[1]*cos+vb[1]*sin;
			x[181-i] = p3[0] + va[0]*cos*radius2a+vb[0]*sin*radius2b;
			y[181-i] = p3[1] + va[1]*cos*radius2a+vb[1]*sin*radius2b;
		}
		ColouredPolygon spiral = new ColouredPolygon(x, y, Color.white);
		// don't add the spiral yet, it needs to be plotted second. Work out the square first.
		x = new double[] {p2[0], p3[0], p1[0]*maxSin+p3[0]*(1-maxSin), p0[0]*maxSin+p2[0]*(1-maxSin)};
		y = new double[] {p2[1], p3[1], p1[1]*maxSin+p3[1]*(1-maxSin), p0[1]*maxSin+p2[1]*(1-maxSin)};
		ColouredPolygon square = new ColouredPolygon(x, y, getColor(index));
		rtn.add(square);
		// now add the spiral.
		rtn.add(spiral);
		return rtn;
	}

	@Override
	public List<ColouredPolygon> getStartingPolygons(double[] vec, int index) {
		double[] p2 = {vec[4],vec[5]};
		double[] p3 = {vec[6],vec[7]};
		double[] p4 = {vec[8],vec[9]};
		double[] p5 = {vec[10],vec[11]};
		double[] x = new double[] {p2[0], p3[0], p5[0], p4[0]};
		double[] y = new double[] {p2[1], p3[1], p5[1], p4[1]};
		ColouredPolygon square = new ColouredPolygon(x, y, getColor(index));
		return Arrays.asList(square);
	}



	private Color getColor(int index) {
		return OldGoldenSpiral.generatePastels(numberOfColors)[MathUtils.mod(index, numberOfColors)];
	}

	public static void main(String[] args) {
		final int width = 1200;
		final int height = 1200;
		ScaleAnimation zoomingScale = new ScaleAnimation() {
			@Override
			public Scale getScale(double t) {
				return new AxisAlignedScale(width, height, 0.0, 0.0, Math.pow(MathUtils.PHI, t));
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
		File folder = new File("/home/hartleym/Desktop/Animation/Fibo/");
		String filePrefix = "fibo";
		double frameRate = 25;
		boolean scaleOutput = false;
		double[] square = {0,1,0,0,1,1,1,0,4,1,4,0};
		GoldenSpiral animation = new GoldenSpiral(square,0, 20, 1, 5);
		new Animate(animation, zoomingScale, folder, filePrefix, frameRate, scaleOutput).makeClip();
	}

	
}
