package com.teasearch.animation;

import java.awt.Graphics;

public class Cutoff implements Animable{

	private Animable delegate;
	private double cutoffTime;

	public Cutoff(Animable delegate, double cutoffTime) {
		this.delegate = delegate;
		this.cutoffTime = cutoffTime;
	}

	@Override
	public double getDuration() {
		return cutoffTime;
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		if (t > cutoffTime) return;
		delegate.draw(gr, scale, t);
	}
	

}
