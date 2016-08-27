package com.teasearch.animation.ingress.fields;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Arrays;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Line;
import com.teasearch.animation.Pause;
import com.teasearch.animation.PolygonFade;
import com.teasearch.animation.Series;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.utils.GeomUtils;

public class ActiveField extends ActiveObject{

	private Portal p1;
	private Portal p2;
	private Portal p3;
	private Color c;
	private double creationTime;
	private double area;

	public ActiveField(double created, Portal p1, Portal p2, Portal p3, Color c, double area) {
		this.creationTime = created;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.c = c;
		this.area = area;
	}
	
	@Override
	public Animable getBaseAnimation() {
		return new Series(
				new Pause(creationTime),
				new PolygonFade(c, new double[] {p1.getX(), p2.getX(), p3.getX()}, new double[] {p1.getY(), p2.getY(), p3.getY()}, 0.2)
				);

	}

	public boolean covers(Portal p) {
		if (p.equals(p1) || p.equals(p2) || p.equals(p3)) {
			return false;
		}
		return GeomUtils.pointInTriangle(p.getPt(), p1.getPt(), p2.getPt(), p3.getPt());
	}

	@Override
	public Portal[] getPortals() {
		return new Portal[] {p1,p2,p3};
	}

	public String toString() {
		return "F"+Arrays.toString(getPortals());
	}

}
