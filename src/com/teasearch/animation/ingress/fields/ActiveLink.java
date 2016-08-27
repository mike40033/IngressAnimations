package com.teasearch.animation.ingress.fields;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Arrays;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Line;
import com.teasearch.animation.Pause;
import com.teasearch.animation.Series;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.utils.GeomUtils;


public class ActiveLink extends ActiveObject{

	private Portal p1;
	private Portal p2;
	private Color c;
	private double creationTime;

	public ActiveLink(double created, Portal p1, Portal p2, Color c) {
		this.creationTime = created;
		this.p1 = p1;
		this.p2 = p2;
		this.c = c;
	}
	
	@Override
	public Animable getBaseAnimation() {
		return new Series(
				new Pause(creationTime),
				new Line(c, new BasicStroke(3f), p1.getX(), p1.getY(), p2.getX(), p2.getY(), 0.2)
				);
	}

	public boolean isBetween(Portal src, Portal dest) {
		if (src.equals(p1) && dest.equals(p2)) return true;
		if (src.equals(p2) && dest.equals(p1)) return true;
		return false;
	}

	public boolean crosses(Portal src, Portal dest) {
		if (p1.equals(src) || p1.equals(dest) || p2.equals(src) || p2.equals(dest)) return false;
		return GeomUtils.lineSegmentsCross(src.getPt(), dest.getPt(), p2.getPt(), p1.getPt());
	}

	public Portal getOtherPortal(Portal src) {
		if (p1.equals(src)) return p2;
		if (p2.equals(src)) return p1;
		return null;
	}

	@Override
	public Portal[] getPortals() {
		return new Portal[] {p1,p2};
	}
	
	public String toString() {
		return "L"+Arrays.toString(getPortals());
	}

}
