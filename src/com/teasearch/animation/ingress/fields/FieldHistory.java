package com.teasearch.animation.ingress.fields;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Parallel;
import com.teasearch.animation.ingress.mu.methods.MUMethod;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalAnimation;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;

public class FieldHistory {

	private PortalHistory[] histories;

	public FieldHistory(PortalHistory[] histories) {
		this.histories = histories;
	}
	
	public Animable getAnimation(MUMethod areaPerMU, Map<Color, Corner> panelLocations) {
		List<Animable> streams = new LinkedList<>();
		// create the portal animations
		LinkedHashMap<Portal, PortalAnimation> animations = new LinkedHashMap<>();
		for (int i=0; i<histories.length; i++) {
			PortalAnimation portalAnimation = new PortalAnimation(histories[i]);
			animations.put(histories[i].getPortal(), portalAnimation);
		}
		
		LinkAndFieldAnimation linksAndFields = new LinkAndFieldAnimation(histories, animations, areaPerMU, panelLocations);
		streams.add(linksAndFields);
		for (PortalAnimation portalAnimation : animations.values()) {
			streams.add(portalAnimation);
		}
		return new Parallel(streams.toArray(new Animable[0]));
	}

}
