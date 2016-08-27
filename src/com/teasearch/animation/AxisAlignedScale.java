package com.teasearch.animation;

public class AxisAlignedScale extends Scale {
	private double cx;
	private double cy;
	private double rx;
	private double ry;

	public AxisAlignedScale(int width, int height, double cx, double cy, double rx) {
		super(width, height);
		this.cx = cx;
		this.cy = cy;
		this.rx = rx;
		this.ry = rx*height/width;
	}

	@Override
	public int[] toGraphics(double x, double y) {
		int ix = (int) Math.round((x - cx)/rx * width/2 + width/2);
		int iy = (int) Math.round((cy - y)/ry * height/2 + height/2);
		return new int[] {ix,iy};
	}

	@Override
	public double[] fromGraphics(int ix, int iy) {
		double x = cx + (ix-width/2)*2.0 / width * rx;
		double y = cy - (iy-height/2)*2.0 / height * ry;
		return new double[] {x,y};
	}
}
