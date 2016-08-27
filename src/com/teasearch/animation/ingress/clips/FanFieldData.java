package com.teasearch.animation.ingress.clips;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.Instruction;
import com.teasearch.animation.ingress.agent.instructions.LabelPortal;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;
import com.teasearch.utils.GeomUtils;

public class FanFieldData {

	private LinkedList<Instruction> instructions;
	private PortalHistory[] otherPortal;
	private List<PortalHistory>[] linkTargets;

	public FanFieldData(LinkedList<Instruction> instructions,
			PortalHistory[] otherPortal, List<PortalHistory>[] linkTargets) {
				this.instructions = instructions;
				this.otherPortal = otherPortal;
				this.linkTargets = linkTargets;
	}

	public List<Instruction> getInstructions() {
		return instructions;
	}
	
	public PortalHistory[] getRebuildLinkOrder() {
		LinkedList<PortalHistory> rtn = new LinkedList<>();
		for (int i=0; i<otherPortal.length; i++) {
			if (i == 0) {
				rtn.add(otherPortal[i]);
			}
			for (int j=otherPortal.length-1; j>i; j--) {
				if (rtn.contains(otherPortal[j])) continue;
				if (!linkTargets[j].contains(otherPortal[i])) continue;
				rtn.add(otherPortal[j]);
			}
		}
		return rtn.toArray(new PortalHistory[0]);
	}
	
	public static FanFieldData createFanfieldData(
			PortalHistory anchor,
			boolean captureAnchor,
			List<PortalHistory> others,
			boolean captureOthers,
			FanType fanType,
			boolean clockwise,
			boolean addLabels,
			double labelDelay,
			double fanDelay,
			double linkDelay
			) {
		LinkedList<Instruction> rtn = new LinkedList<>();
		// sort the other portals
		TreeMap<Double, PortalHistory> map = new TreeMap<>();
		TreeMap<Double,PortalHistory> reversed = new TreeMap<>(new Comparator<Double>() {
			@Override
			public int compare(Double arg0, Double arg1) {
				return -arg0.compareTo(arg1);
			}
		});
		for (PortalHistory other : others) {
			double theta = Math.atan2(other.getY()-anchor.getY(), other.getX()-anchor.getX());
			if (!map.isEmpty()) {
				double otherTheta = map.firstKey();
				while (theta > otherTheta+Math.PI) {
					theta -= 2*Math.PI;
				}
				while (theta < otherTheta-Math.PI) {
					theta += 2*Math.PI;
				}
			}
			map.put(theta, other);
			reversed.put(theta, other);
		}
		// reverse if needed
		if (clockwise) {
			TreeMap<Double, PortalHistory> tmp = map;
			map = reversed;
			reversed = tmp;
		}
		// figure out the corner for the labels
		double sum = 0;
		for (double d : map.keySet()) {
			sum += d;
		}
		sum /= map.size();
		sum /= Math.PI/2;
		int quad = (int)Math.floor(sum) % 4;
		if (quad < 0) quad+=4;
		Corner[] corners = {Corner.SW, Corner.SE, Corner.NE, Corner.NW};
		Corner corner = corners[quad];
		// capture as needed, then do the fan
		switch (fanType) {
		case out:
			if (captureOthers) {
				for (PortalHistory h : map.values()) {
					rtn.add(new CapturePortal(h));
				}
			}
			if (captureAnchor) {
				rtn.add(new CapturePortal(anchor));
			}
			if (addLabels) {
				rtn.add(new LabelPortal(anchor, "A", corner));
			}
			rtn.add(new LinkPortals(anchor, fanDelay, others.toArray(new PortalHistory[0])));
			break;
		case in:
			if (captureAnchor) {
				rtn.add(new CapturePortal(anchor));
			}
			if (addLabels) {
				rtn.add(new LabelPortal(anchor, "A", corner));
			}
			if (captureOthers) {
				for (PortalHistory h : reversed.values()) {
					rtn.add(new CapturePortal(h));
					rtn.add(new LinkPortals(h, fanDelay, anchor));
				}
			}
			break;
		case concurrent:
			if (captureAnchor) {
				rtn.add(new CapturePortal(anchor));
			}
			if (addLabels) {
				rtn.add(new LabelPortal(anchor, "A", corner));
			}
			// that's all for now.
			break;
		}
		// get other portals and figure out which ones they should be linked to 
		PortalHistory[] otherPortal = map.values().toArray(new PortalHistory[0]);
		boolean concurrent = FanType.concurrent.equals(fanType);
		if (addLabels && !concurrent) {
			for (int i=0; i<otherPortal.length; i++) {
				rtn.add(Wait._for(labelDelay/2, false));
				rtn.add(new LabelPortal(otherPortal[i], ""+(i+1), corner.opposite()));
				rtn.add(Wait._for(labelDelay/2, false));
			}
		}
		List<PortalHistory>[] linkTargets = new List[otherPortal.length];
		boolean[] covered = new boolean[otherPortal.length];
		for (int i=0; i<linkTargets.length; i++) {
			TreeSet<Integer> linkTargetIndices = new TreeSet<>();
			linkTargets[i] = new LinkedList<>();
			if (concurrent) {
				linkTargets[i].add(anchor);
			}
			for (int j=0; j<i; j++) {
				boolean okay = !covered[j];
				for (int k=j+1; k<i; k++) {
					okay &= !GeomUtils.lineSegmentsCross(otherPortal[j].getPt(), otherPortal[i].getPt(), otherPortal[k].getPt(), anchor.getPt());
				}
				if (okay) {
					linkTargetIndices.add(j);
				}
			}
			for (int j : linkTargetIndices) {
				linkTargets[i].add(otherPortal[j]);
				if (!linkTargetIndices.first().equals(j)) {
					covered[j] = true;
				}
			}
		}
		// now, actually do the linking:
		for (int i=0; i<otherPortal.length; i++) {
			if (linkTargets[i].isEmpty() && !concurrent) {
				continue;
			}
			// go to the portal
			rtn.add(concurrent ? new CapturePortal(otherPortal[i]) : new GoTo(otherPortal[i]));
			if (addLabels && concurrent) {
				rtn.add(Wait._for(labelDelay/2, false));
				rtn.add(new LabelPortal(otherPortal[i], ""+(i+1), corner.opposite()));
				rtn.add(Wait._for(labelDelay/2, false));
			}
			rtn.add(new LinkPortals(otherPortal[i], linkDelay, linkTargets[i].toArray(new PortalHistory[0])));
		}
		return new FanFieldData(rtn, otherPortal, linkTargets);
	}
	
}