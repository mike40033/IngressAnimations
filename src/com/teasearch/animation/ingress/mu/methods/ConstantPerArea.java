package com.teasearch.animation.ingress.mu.methods;

import com.teasearch.animation.ingress.portals.Portal;

public class ConstantPerArea implements MUMethod {

	public ConstantPerArea(double areaPerMU) {
		this.areaPerMU = areaPerMU;
	}

	private double areaPerMU;

	@Override
	public int calcMU(double area, Portal src, Portal dest, Portal other) {
		int mu = (int) Math.round(area / areaPerMU);
		if (mu == 0) mu=1;
		return mu;
	}

}
