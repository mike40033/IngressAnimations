package com.teasearch.animation.ingress.clips.youtube;

import java.awt.Color;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.utils.GeomUtils;

public class Herringbone extends IngressAnimableConstantMU {


	@Override
	protected void addInstructions(InstructionList[] lists, PortalHistory[] histories) {
		InstructionList list = lists[0];
		list.addInstruction(new CapturePortal(histories[0]));
		list.addInstruction(new CapturePortal(histories[1]));
		list.addInstruction(new LinkPortals(histories[1], 0.1, histories[0]));
		list.addInstruction(new CapturePortal(histories[2]));
		list.addInstruction(new LinkPortals(histories[2], 1, new PortalHistory[] {histories[0], histories[1]}));
		for (int j=3; j<getNumberOfPortalsInField(); j++) {
			list.addInstruction(new CapturePortal(histories[j]));
			list.addInstruction(new LinkPortals(histories[j], 1, new PortalHistory[] {histories[0], histories[1], histories[j-1]}));
		}
		list.addInstruction(Wait._for(1, true));
		list.addInstruction(new CapturePortal(histories[1]));
		
		List<PortalHistory> targetList = new LinkedList<>();
		targetList.add(histories[0]);
		for (int j=getNumberOfPortalsInField()-1; j>=2; j--) {
			targetList.add(histories[j]);
		}
		PortalHistory[] linkTargets = targetList.toArray(new PortalHistory[0]);
		list.addInstruction(new LinkPortals(histories[1], 0.75, linkTargets));
		list.addInstruction(new GoTo(900, 400, false));
		
		list = lists[1];
		list.addInstruction(Wait._until(62, false));
		list.addInstruction(new CapturePortal(histories[1]));
		list.addInstruction(Wait._for(0.6, true));
		list.addInstruction(new GoTo(900, -400, false));
		
	}

	private int getNumberOfPortalsInField() {
		return 12;
	}

	@Override
	protected Agent[] getAgents() {
		return new Agent[] {
				new Agent(Faction.RES, -400, 0, Math.PI/4),
				new Agent(Faction.ENL, 900, 250, -Math.PI)
				};
	}

	@Override
	protected Portal[] getPortals() {
		Random rr = new Random(135769);
		List<Portal> rtn = new LinkedList<>();
		Portal first = new Portal(-550, -500, Color.gray);
		rtn.add(first);
		Portal second = new Portal(550, -520, Color.gray);
		rtn.add(second);
		Portal last = null;
		for (int i=1; i<11; i++) {
			rtn.add(last = new Portal(jitter(rr), -500+100*i+jitter(rr), Color.gray));
		}
		while (rtn.size() < 20) {
			double[] c = last.getPt();
			double[] b = second.getPt();
			double[] a = first.getPt();
			double xx = rr.nextGaussian();
			double yy = rr.nextGaussian();
			xx = Math.sqrt(xx*xx+yy*yy);
			xx = (yy < 0 ? -xx : xx);
			double[] p = {xx*300, rr.nextGaussian()*300};
			if (GeomUtils.pointInTriangle(p, a, b, c)) {
				rtn.add(new Portal(p[0], p[1], Color.gray));
			}
		}
		return rtn.toArray(new Portal[0]);
	}

	
	private double jitter(Random rr) {
		return rr.nextGaussian()*15;
	}

	@Override
	protected double getAreaPerMU() {
		Portal[] portals = getPortals();
		double area = GeomUtils.getArea(portals[0].getPt(), portals[1].getPt(), portals[getNumberOfPortalsInField()-1].getPt());
		return area/1000;
	}

}
