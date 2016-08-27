package com.teasearch.animation.ingress.mu;

import java.util.List;

import com.teasearch.animation.ingress.portals.Portal;

public abstract class MUOptimiser {
	public abstract List<Portal> findBestSplittingOrder(Portal[] triangle, List<Portal> interior);
}
