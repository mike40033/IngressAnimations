package com.teasearch.animation.ingress.fields;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.teasearch.animation.Animable;
import com.teasearch.animation.ingress.portals.Destroy;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.stats.StatHistory;
import com.teasearch.utils.GeomUtils;
import com.teasearch.utils.MapUtils;

public class FieldState {

	LinkedHashMap<Portal, List<ActiveObject>> objects = new LinkedHashMap<>();
	
	public List<Animable> destroyPortal(double time, Destroy destroy, StatHistory statHistory) {
		Portal portal = destroy.getPortal();
		// 1) find all links and fields attached to this portal
		List<ActiveObject> toDestroy = objects.get(portal);
		if (toDestroy == null) {
			return new LinkedList<>();
		}
		toDestroy = new LinkedList<>(toDestroy);
		for (ActiveObject o : toDestroy) {
			for (Portal p : o.getPortals()) {
				objects.get(p).remove(o);
			}
			// update stats
			if (o instanceof ActiveLink) {
				statHistory.destroyLinks(1, time);
			} else if (o instanceof ActiveField) {
				statHistory.destroyFields(1, time);
			}
		}
		
		// 2) create animations destroying them
		List<Animable> rtn = new LinkedList<>();
		for (ActiveObject object : toDestroy) {
			rtn.add(object.destroy(time));
		}
		return rtn;
	}

	public void attemptLink(double time, Portal src, Portal dest, Color color, StatHistory statHistory) {
		// assume color has already been checked.
		for (List<ActiveObject> list : objects.values()) {
			for (ActiveObject object : list) {
				if (object instanceof ActiveField) {
					// if the source portal is inside a field, reject
					if (((ActiveField)object).covers(src)) {
						return;
					}
				}
				if (object instanceof ActiveLink) {
					ActiveLink link = (ActiveLink)object;
					// if link already exists, reject
					if (link.isBetween(src, dest)) {
						return;
					}
					// if the link crosses another, reject.
					if (link.crosses(src,dest)) {
						return;
					}
				}
			}
		}
		// outgoing link limits not supported yet
		// if link created, check for fields:
		ActiveLink link = new ActiveLink(time, src, dest, color);
		// bump the stats:
		statHistory.createLink(time);
		// 1) find all portals already linked to src
		Set<Portal> fieldCandidates = getLinkedPortals(src);
		// 2) find all portals already linked to dest
		// 3) do a retainAll
		fieldCandidates.retainAll(getLinkedPortals(dest));
		// add the active link, before we forget
		MapUtils.insert(objects, src, link);
		MapUtils.insert(objects, dest, link);
		// 4) sort by <strike>perp height</strike> area, ie, by cross product
		TreeMap<Double, Portal> ranked = new TreeMap<>();
		double[] vector1 = {dest.getX()-src.getX(), dest.getY()-src.getY()};
		for (Portal p : fieldCandidates) {
			double[] vector2 = {p.getX()-src.getX(), p.getY()-src.getY()};
			double area = GeomUtils.crossProduct(vector1, vector2)/2;
			ranked.put(area, p);
		}
		// 5) greatest +ve and -ve give fields.
		List<Double> fieldAnchors = new LinkedList<>();
		if (!ranked.isEmpty() && ranked.firstKey() < 0) {
			fieldAnchors.add(ranked.firstKey());
		}
		if (!ranked.isEmpty() && ranked.lastKey() > 0) {
			fieldAnchors.add(ranked.lastKey());
		}
		for (Double area : fieldAnchors) {
			Portal anchor = ranked.get(area);
			ActiveField field = new ActiveField(time, src, dest, anchor, color, Math.abs(area));
			MapUtils.insert(objects, src, field);
			MapUtils.insert(objects, dest, field);
			MapUtils.insert(objects, anchor, field);
			// bump the stats:
			statHistory.createField(time, Math.abs(area), src, dest, anchor);
		}
	}

	private Set<Portal> getLinkedPortals(Portal src) {
		LinkedHashSet<Portal> rtn = new LinkedHashSet<>();
		List<ActiveObject> list = objects.get(src);
		if (list == null) {
			return rtn;
		}
		for (ActiveObject obj : list) {
			if (obj instanceof ActiveLink) {
				Portal other = ((ActiveLink)obj).getOtherPortal(src);
				if (other == null) {
					throw new IllegalStateException("null found!");
				}
				rtn.add(other);
			}
		}
		return rtn;
	}

	public List<Animable> getFinalAnimations() {
		LinkedList<Animable> rtn = new LinkedList<>();
		LinkedHashSet<Portal> portalsDone = new LinkedHashSet<>();
		for (Portal p : objects.keySet()) {
			for (ActiveObject o : objects.get(p)) {
				Portal[] pp = o.getPortals();
				boolean done = false;
				for (Portal q : pp) {
					if (portalsDone.contains(q)) {
						done = true;
					}
				}
				if (!done) {
					rtn.add(o.getBaseAnimation());
				}
			}
			portalsDone.add(p);
		}
		return rtn;
	}

}
