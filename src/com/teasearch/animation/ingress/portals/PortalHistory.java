package com.teasearch.animation.ingress.portals;

import java.awt.Color;
import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.ingress.stats.Corner;
import com.teasearch.utils.MapUtils;

public class PortalHistory {
	private Portal portal;
	private TreeMap<Double,List<PortalEvent>> events;
	public PortalHistory(Portal portal) {
		this.portal = portal;
		this.events = new TreeMap<>();
	}
	public double getX() {
		return portal.getX();
	}
	public double getY() {
		return portal.getY();
	}
	public void addCapture(double time, Color color) {
		MapUtils.insert(events, time, new Capture(color));
	}
	public void addDestroy(double time, Color intendedColor) {
		MapUtils.insert(events, time, new Destroy(portal, intendedColor));
	}
	public Portal getPortal() {
		return portal;
	}
	public TreeMap<Double, List<PortalEvent>> getEvents() {
		return events;
	}
	public void addLinkTo(double time, Color color, PortalHistory other) {
		MapUtils.insert(events, time, new Link(this, other, color));
		
	}
	public void setLabel(double time, String text, Corner corner, Color color) {
		MapUtils.insert(events, time, new LabelChange(text, corner, color));
	}
	public double[] getPt() {
		return portal.getPt();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof PortalHistory)) return false;
		return this.portal.equals(((PortalHistory)o).portal);
	}
	
	public String toString() {
		return portal.toString();
	}
}
