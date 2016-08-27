package com.teasearch.animation;

import java.awt.Graphics;

public abstract class AnimableItem implements Animable{

	private double duration;

	public AnimableItem(double duration) {
		this.duration = duration;
	}
	
	@Override
	public double getDuration() {
		return duration;
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		if (t <= 0) {
			drawInitial(gr, scale.getScale(t));
		} else if (t > duration) {
			drawFinal(gr, scale.getScale(t));
		} else {
			drawIntermediate(gr, scale.getScale(t), t/duration);
		}
	}

	protected abstract void drawIntermediate(Graphics gr, Scale scale, double u);

	protected abstract void drawInitial(Graphics gr, Scale scale);

	protected abstract void drawFinal(Graphics gr, Scale scale);

}
