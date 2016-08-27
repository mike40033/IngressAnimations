package com.teasearch.animation.ingress.portals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import com.teasearch.animation.Animable;
import com.teasearch.animation.AxisAlignedScale;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.ingress.mu.methods.MUMethod;
import com.teasearch.animation.ingress.stats.StatHistory;

public class PortalAnimation implements Animable {

	private TreeMap<Double, Color> changes;
	private TreeMap<Double, Label> labels;
	private double px;
	private double py;

	public PortalAnimation(PortalHistory portalHistory) {
		TreeMap<Double, List<PortalEvent>> events = portalHistory.getEvents();
		TreeMap<Double, PortalOwnerChange> ownershipChanges = new TreeMap<>();
		// filter out everything but captures
		for (double d : events.keySet()) {
			for (PortalEvent pe : events.get(d)) {
				if (pe instanceof PortalOwnerChange) {
					ownershipChanges.put(d, (PortalOwnerChange) pe);
				}
			}
		}
		// figure out the Color changes
		TreeMap<Double, Color> changes = new TreeMap<>();
		Color currentColor = portalHistory.getPortal().getInitialColor();
		changes.put(Double.NEGATIVE_INFINITY, currentColor);
		for (double d : ownershipChanges.keySet()) {
			PortalOwnerChange c = ownershipChanges.get(d);
			Color target = c.getColor();
			Color intendedTarget = c.getIntendedColor();
			// don't do anything if the portal is already the right color
			if (target.equals(currentColor) || intendedTarget.equals(currentColor)) {
				continue;
			}
			// can't capture a portal that's not gray
			if (!target.equals(Color.gray) && !currentColor.equals(Color.gray)) {
				continue;
			}
			// update the portal color
			changes.put(d, target);
			currentColor = target;
		}
		this.changes = changes;
		this.px = portalHistory.getX();
		this.py = portalHistory.getY();
		// and the labels:
		TreeMap<Double, Label> labels = new TreeMap<>();
		// filter out everything but captures
		for (double d : events.keySet()) {
			for (PortalEvent pe : events.get(d)) {
				if (pe instanceof LabelChange) {
					labels.put(d, ((LabelChange)pe).getLabel());
				}
			}
		}
		this.labels = labels;
	}

	@Override
	public double getDuration() {
		return changes.lastKey();
	}

	public Map<Color, StatHistory> getPortalStats(MUMethod areaPerMU) {
		Map<Color, StatHistory> rtn = new LinkedHashMap<>();
		Color prev = null;
		for (Double time : changes.keySet()) {
			Color curr = changes.get(time);
			if (prev == null) {
				prev = curr;
				continue;
			}
			Color key;
			if (prev.equals(Color.gray)) {
				key = curr;
			} else if (curr.equals(Color.gray)) {
				NavigableMap<Double, Color> tailMap = changes.tailMap(time, false);
				if (tailMap == null) {
					throw new IllegalStateException("Can't leave portals grey yet!");
				}
				key = tailMap.firstEntry().getValue();
			} else {
				key = prev;
			}
			if (!rtn.containsKey(key)) {
				rtn.put(key, new StatHistory(key, areaPerMU));
			}
			StatHistory statHistory = rtn.get(key);
			if (curr.equals(Color.gray)) {
				statHistory.destroyPortal(time);
			} else {
				statHistory.capturePortal(time);
			}
			prev = curr;
		}
		return rtn;
	}


	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		double portalTop = py+12; 
		double portalBot = py-12;
		double portalLeft = px-12;
		double portalRigt = px+12;
		if (! (scale instanceof AxisAlignedScale)) {
			throw new UnsupportedOperationException("portal circle calculations are not supported for scale "+scale.getClass());
		}
		int[] topLeft = scale.getScale(t).toGraphics(portalLeft, portalTop);
		int[] botRigt = scale.getScale(t).toGraphics(portalRigt, portalBot);
		int height = botRigt[1] - topLeft[1]; 
		int width = botRigt[0] - topLeft[0]; 
		Color c = getColor(t);
		gr.setColor(c.darker());
		gr.fillOval(topLeft[0], topLeft[1], width, height);
		gr.setColor(c);
		((Graphics2D) gr).setStroke(new BasicStroke(width/6f));
		gr.drawOval(topLeft[0], topLeft[1], width, height);
		Label label = getLabel(t);
		if (label == null) {
			return;
		}
		gr.setFont(gr.getFont().deriveFont(height*1.5f));
		label.draw(gr, topLeft, botRigt, c);
		
	}

	private Label getLabel(double t) {
		SortedMap<Double, Label> headMap = labels.headMap(t);
		if (headMap.isEmpty()) return null;
		double u = headMap.lastKey();
		return labels.get(u);
	}

	public Color getColor(double t) {
		double u = changes.headMap(t).lastKey();
		return changes.get(u);
	}
	
}
