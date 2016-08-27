package com.teasearch.animation.ingress.fields;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Cutoff;
import com.teasearch.animation.ingress.portals.Portal;

public abstract class ActiveObject {

	public Animable destroy(double time) {
		return new Cutoff(getBaseAnimation(), time);
	}
	
	public abstract Animable getBaseAnimation();

	public abstract Portal[] getPortals();
	

}
