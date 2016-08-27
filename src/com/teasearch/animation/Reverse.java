package com.teasearch.animation;

import java.awt.Graphics;

public class Reverse implements Animable {

	private Animable delegate;

	public Reverse(Animable delegate) {
		this.delegate = delegate;
	}

	@Override
	public double getDuration() {
		return delegate.getDuration();
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		delegate.draw(gr, scale, getDuration()-t);
	}

}
