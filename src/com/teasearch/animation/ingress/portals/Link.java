package com.teasearch.animation.ingress.portals;

import java.awt.Color;

import com.teasearch.animation.ingress.fields.LinkStateChange;

public class Link extends PortalEvent implements LinkStateChange{

	private Portal from;
	private Portal to;
	private Color color;

	public Link(PortalHistory from, PortalHistory to, Color color) {
		this.from = from.getPortal();
		this.to = to.getPortal();
		this.color = color;
	}

	public Portal getFrom() {
		return from;
	}
	public Portal getTo() {
		return to;
	}

	public Color getColor() {
		return color;
	}

}
