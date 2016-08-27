package com.teasearch.animation;

import java.awt.Graphics;

public class Series implements Animable {

	private Animable[] children;
	private double duration;
	public Series(Animable...children) {
		this.children = children;
		double duration = 0;
		for (Animable a : children) {
			duration += a.getDuration();
		}
		this.duration = duration;
	}
	
	@Override
	public double getDuration() {
		return duration;
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		for (Animable a : children) {
			a.draw(gr, scale, t);
			t -= a.getDuration();
		}
	}

}
