package com.teasearch.animation.ingress.clips;

import com.teasearch.animation.ingress.mu.methods.ConstantPerArea;
import com.teasearch.animation.ingress.mu.methods.MUMethod;

public abstract class IngressAnimableConstantMU extends IngressAnimable {


	protected MUMethod getMUCalculationMethod(){
		return new ConstantPerArea(getAreaPerMU());
	}
	
	protected abstract double getAreaPerMU();

}
