package com.teasearch.animation.ingress.portals;

import java.awt.Color;

import com.teasearch.animation.ingress.stats.Corner;

public class LabelChange extends PortalEvent {

	private Color color;
	private String text;
	private Corner corner;

	public LabelChange(String text, Corner corner, Color color) {
		this.text = text;
		this.corner = corner;
		this.color = color;
	}

	public Label getLabel() {
		return new Label(text, corner, color);
	}

}
