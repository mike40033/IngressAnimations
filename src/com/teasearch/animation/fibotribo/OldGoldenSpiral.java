package com.teasearch.animation.fibotribo;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

import com.teasearch.animation.Animable;
import com.teasearch.animation.AxisAlignedScale;
import com.teasearch.animation.Reverse;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.clips.Animate;
import com.teasearch.utils.MathUtils;

public class OldGoldenSpiral implements Animable {

	private int endSquare;
	private int startSquare;
	private double timePerSquare;
	private Color[] colors;

	public OldGoldenSpiral(int startSquare, int endSquare, double timePerSquare, Color[] colors) {
		this.startSquare = startSquare;
		this.endSquare = endSquare;
		this.timePerSquare = timePerSquare;
		this.colors = colors;
		
	}
	
	@Override
	public double getDuration() {
		return endSquare*timePerSquare;
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		gr.setColor(Color.white);
		gr.fillRect(0, 0, scale.getWidth(), scale.getHeight());
		int maxFullSquare = (int) Math.floor(t);
		drawSquare(gr, (maxFullSquare+1), scale.getScale(t), t - Math.floor(t)); 
		for (int square=maxFullSquare; square >= startSquare; square--) {
			drawSquare(gr, square, scale.getScale(t), 1); 
		}
	}

	private void drawSquare(Graphics gr, int square, Scale scale, double frac) {
		gr.setColor(colors[MathUtils.mod(square,colors.length)]);
		int index = MathUtils.mod(square, 4);
		int power = (square-index)/4;
		double[][] pp = base[index];
		double[][] qq = base[index+1];
		double multiple = Math.pow(3*p+2,power);
		int[] ix = new int[4];
		int[] iy = new int[4];
		for (int i=0; i<4; i++) {
			double x = (pp[i][0]*(1-frac) + qq[i][0]*frac)*multiple;
			double y = (pp[i][1]*(1-frac) + qq[i][1]*frac)*multiple;
			int[] ixy = scale.toGraphics(x, y);
			ix[i] = ixy[0];
			iy[i] = ixy[1];
		}
		gr.fillPolygon(ix, iy, 4);
		
	}

	// constants //
	
	public final static double p = (Math.sqrt(5)+1)/2;
	public static final double[][][] base =  {
			{{-1,-p},{p+1,-p},{p+1,2*p+1},{-1,2*p+1}},
			{{p+1,-p},{p+1,2*p+1},{-3*p-2,2*p+1},{-3*p-2,-p}},
			{{p+1,2*p+1},{-3*p-2,2*p+1},{-3*p-2,-5*p-3},{p+1,-5*p-3}},
			{{-3*p-2,2*p+1},{-3*p-2,-5*p-3},{8*p+5,-5*p-3},{8*p+5,2*p+1}},
	        {{-3*p-2,-5*p-3},{8*p+5,-5*p-3},{8*p+5,13*p+8},{-3*p-2,13*p+8}}
	}; 
	
	public static void main(String[] args) {
		final int width = 800;
		final int height = 800;
		ScaleAnimation zoomingScale = new ScaleAnimation() {
			@Override
			public Scale getScale(double t) {
				return new AxisAlignedScale(width, height, 0.0, 0.0, Math.pow(p, t)*6.8);
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
		Color[] colors = generatePastels(12);
		File folder = new File("/home/hartleym/Desktop/Animation/Fibo/");
		String filePrefix = "golden";
		double frameRate = 25;
		boolean scaleOutput = false;
		new Animate(new Reverse(new OldGoldenSpiral(-100, 12, 1, colors)), zoomingScale, folder, filePrefix, frameRate, scaleOutput).makeClip();
	}


	public static Color[] generatePastels(int n) {
		Color[] cc = new Color[n];
		for (int i=0; i<cc.length; i++) {
			cc[i] = new Color(Color.HSBtoRGB(((float)i)/n, 0.76f,1f));
		}
		return cc;
	}
}
