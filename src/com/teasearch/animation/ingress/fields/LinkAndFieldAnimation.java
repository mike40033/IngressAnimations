package com.teasearch.animation.ingress.fields;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Parallel;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.ingress.mu.methods.MUMethod;
import com.teasearch.animation.ingress.portals.Destroy;
import com.teasearch.animation.ingress.portals.Link;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalAnimation;
import com.teasearch.animation.ingress.portals.PortalEvent;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;
import com.teasearch.animation.ingress.stats.StatHistory;
import com.teasearch.utils.MapUtils;

public class LinkAndFieldAnimation implements Animable {

	private Parallel delegate;

	public LinkAndFieldAnimation(PortalHistory[] histories,
			LinkedHashMap<Portal, PortalAnimation> animations,
			MUMethod areaPerMU,
			Map<Color, Corner> statPanelLocations) {
		Map<Color, StatHistory> statHistories = new LinkedHashMap<>();
		for (PortalAnimation animation : animations.values()) {
			StatHistory.merge(statHistories, animation.getPortalStats(areaPerMU));
		}
		TreeMap<Double, List<LinkStateChange>> map = new TreeMap<>();
		// 1) look through the histories and extract Link and Destroy objects
		// 1.1) put them into a treemap<Double>, so I have them in chronological order
		for (PortalHistory ph : histories) {
			TreeMap<Double, List<PortalEvent>> events = ph.getEvents();
			for (Double d : events.keySet()) {
				for (PortalEvent p : events.get(d)) {
					if (p instanceof LinkStateChange) {
						MapUtils.insert(map, d, (LinkStateChange)p);
					}
				}
			}
		}
		// 2) create a FieldState object of some kind
		FieldState state = new FieldState();
		LinkedList<Animable> fieldAnimation = new LinkedList<>();
		// 3) scan through the list of link attempts and portal destructions,
		for (Double time : map.keySet()) {
			for (LinkStateChange lsc : map.get(time)) {
				if (lsc instanceof Destroy) {
					Portal affectedPortal = ((Destroy) lsc).getPortal();
					Color intendedColor = ((Destroy) lsc).getIntendedColor();
					Color currentColor = animations.get(affectedPortal).getColor(time);
					if (intendedColor.equals(currentColor)) {
						// don't destroy portals that are the right color
						continue;
					}
					List<Animable> brokenLinksAndFields = state.destroyPortal(time.doubleValue(),(Destroy)lsc,statHistories.get(intendedColor));
					fieldAnimation.addAll(brokenLinksAndFields);
					continue;
				}
				Link link = (Link)lsc;
				Portal p1 = link.getFrom();
				Portal p2 = link.getTo();
				if (p1.equals(p2)) {
					// can't link a portal to itself
					continue;
				}
				Color c1 = animations.get(p1).getColor(time);
				Color c2 = animations.get(p2).getColor(time);
				Color cl = link.getColor();
				if (!(c1.equals(cl)) || !(c2.equals(cl))) {
					// can't link portals of the wrong color
					continue;
				}
				// any other problems get delegated:
				if (!statHistories.containsKey(cl)) {
					statHistories.put(cl, new StatHistory(cl, areaPerMU));
				}
				state.attemptLink(time, p1,p2,cl, statHistories.get(cl));
			}
		}
		// 4) create field and link animations for objects that still remain at the end
		fieldAnimation.addAll(state.getFinalAnimations());
		for (Color color : statPanelLocations.keySet()) {
			StatHistory statHistory = statHistories.get(color);
			if (statHistory == null) {
				statHistory = new StatHistory(color, areaPerMU);
			}
			fieldAnimation.add(statHistory.animatePanel(new Parallel(fieldAnimation.toArray(new Animable[0])).getDuration(),statPanelLocations.get(color)));
		}
		delegate = new Parallel(fieldAnimation.toArray(new Animable[0]));
	}

	@Override
	public double getDuration() {
		return delegate.getDuration();
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		delegate.draw(gr, scale, t);
	}

}
