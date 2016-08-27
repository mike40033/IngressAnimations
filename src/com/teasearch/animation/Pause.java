package com.teasearch.animation;

import java.awt.Graphics;

public class Pause implements Animable{

	private double duration;

	public Pause(double duration) {
		this.duration = duration;
	}
	
	@Override
	public double getDuration() {
		return duration;
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
	}

}
