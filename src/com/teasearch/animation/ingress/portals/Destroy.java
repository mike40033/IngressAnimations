package com.teasearch.animation.ingress.portals;

import java.awt.Color;

import com.teasearch.animation.ingress.fields.LinkStateChange;

public class Destroy extends PortalOwnerChange implements LinkStateChange {

	private Portal portal;

	public Destroy(Portal portal, Color intendedColor) {
		super(Color.gray, intendedColor);
		this.portal = portal;
	}

	public Portal getPortal() {
		return portal;
	}

}
