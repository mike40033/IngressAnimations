package com.teasearch.animation;

public abstract class Scale implements ScaleAnimation{

	protected int width;
	protected int height;

	public Scale(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public abstract int[] toGraphics(double x, double y);
	
	public abstract double[] fromGraphics(int ix, int iy);

	@Override
	public Scale getScale(double t) {
		return this;
	}
	
}
