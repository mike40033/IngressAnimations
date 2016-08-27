package com.teasearch.animation;

import java.awt.Graphics;

public class Easing implements Animable {

	private Animable delegate;
	private boolean start;
	private boolean end;

	public Easing(Animable delegate, boolean start, boolean end) {
		this.delegate = delegate;
		this.start = start;
		this.end = end;
	}
	
	public Easing(Animable delegate) {
		this(delegate, true, true);
	}

	private double computeEasing(double u) {
		if (u < 0 || u > 1) return u;
		if (start) {
			if (end) {
				return f(u);
			} 
			return f(u/2)*2;
		} 
		if (end) {
			return f((u+1)/2)*2-1;
		}
		return u;
	}

	private double f(double t) {
		return t*t*(5+t*(-10+t*(10-4*t)));
	}
	@Override
	public double getDuration() {
		return delegate.getDuration();
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		t = computeEasing(t/getDuration())*getDuration();
		delegate.draw(gr, scale, t);
	}

}
