package com.teasearch.animation.ingress.portals;

import java.awt.Color;

public class PortalOwnerChange extends PortalEvent {

	private Color intendedOwner;
	private Color owner;

	public PortalOwnerChange(Color owner, Color intendedOwner) {
		this.owner = owner;
		this.intendedOwner = intendedOwner;
	}

	public Color getColor() {
		return owner;
	}

	public Color getIntendedColor() {
		return intendedOwner;
	}

}
