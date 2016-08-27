package com.teasearch.animation.ingress;

import java.awt.Color;

public enum Faction {
	
	RES(new Color(0,183,255)),
	ENL(new Color(3,255,3)),
	RED(new Color(255,3,3)),
	MAUVE(new Color(128,64,240)),
	LEMON(new Color(255,255,32)), 
	CHOC(new Color(78,46,40)),
	CREME_TANGERINE(new Color(221,54,10)),
	MONTELIMAR(new Color(116,88,102)),
	GINGER_SLING(new Color(196,138,98)),
	PINEAPPLE_HEART(new Color(223,186,98)),
	YOGURT(new Color(0xf5dfdc))
	;
	
	private Color c;

	private Faction(Color c) {
		this.c = c;
	}
	
	public Color getColor() {
		return c;
	}
}
