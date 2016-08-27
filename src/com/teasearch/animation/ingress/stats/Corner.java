package com.teasearch.animation.ingress.stats;

public enum Corner {
	NW(true, true),
	NE(false, true),
	SW(true, false),
	SE(false, false),
	;
	private boolean isTop;
	private boolean isLeft;
	private Corner(boolean isLeft, boolean isTop) {
		this.isLeft = isLeft;
		this.isTop = isTop;
	}
	public boolean isLeftAligned() {
		return isLeft;
	}
	public boolean isTopAligned() {
		return isTop;
	}
	public Corner opposite() {
		for (Corner c : values()) {
			if (c.isLeft != isLeft && c.isTop != isTop) return c;
		}
		return null;
	}
	
}
