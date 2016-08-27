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

public class Cobweb extends IngressAnimableConstantMU {

	@Override
	protected void addInstructions(InstructionList[] lists, PortalHistory[] histories) {
		InstructionList list0 = lists[0];
		InstructionList list1 = lists[1];
		InstructionList list2 = lists[2];
		PortalHistory[][] branches = new PortalHistory[3][getNumberOfPortalsPerBranch()];
		for (int wig=0 ; wig<3; wig++) {
			for (int i=0; i<getNumberOfPortalsPerBranch(); i++) {
				branches[wig][i] = histories[1+wig*getNumberOfPortalsPerBranch()+i];
			}
		}
		list0.addInstruction(new CapturePortal(histories[0]));
		list1.addInstruction(Wait._for(3, false));
		list2.addInstruction(Wait._for(5, false));
		for (int i=0; i<getNumberOfPortalsPerBranch(); i++) {
			for (int wig=0; wig<3; wig++) {
				lists[wig].addInstruction(new CapturePortal(branches[wig][i]));
				if (i == 0) {
					if (wig == 0) {
						lists[wig].addInstruction(new LinkPortals(branches[wig][i], 1, histories[0]));
					} else if (wig == 1) {
						lists[wig].addInstruction(new LinkPortals(branches[wig][i], 1, branches[wig-1][0], histories[0]));
					} else if (wig == 2) {
						lists[wig].addInstruction(new LinkPortals(branches[wig][i], 1, branches[wig-2][0], branches[wig-1][0], histories[0]));
					}
				} else {
					if (wig == 0) {
						lists[wig].addInstruction(new LinkPortals(branches[wig][i], 1, branches[1][i-1], branches[2][i-1], branches[0][i-1]));
					} else if (wig == 1) {
						lists[wig].addInstruction(new LinkPortals(branches[wig][i], 1, branches[2][i-1], branches[0][i], branches[1][i-1]));
					} else if (wig == 2) {
						lists[wig].addInstruction(new LinkPortals(branches[wig][i], 1, branches[0][i], branches[1][i], branches[2][i-1]));
					}
				}
				if (i < getNumberOfPortalsPerBranch()-1) {
					lists[wig].addInstruction(Wait._for(4, false));
				}
				
			}
		}
		for (int w=0; w<3; w++) {
			lists[w].addInstruction(new GoTo(900,w*50-50, false));
		}
		
		lists[3].addInstruction(Wait._for(50, false));
		lists[3].addInstruction(new CapturePortal(branches[1][branches[1].length-1]));
		lists[3].addInstruction(Wait._for(4, true));
		lists[3].addInstruction(new CapturePortal(branches[2][(branches[1].length/2)]));
		lists[3].addInstruction(new GoTo(-800,-200,false));
	}

	public int getNumberOfPortalsPerBranch() {
		return 5;
	}

	@Override
	protected Agent[] getAgents() {
		return new Agent[] {
				new Agent(Faction.ENL, -400, 50, Math.PI/6),
				new Agent(Faction.ENL, -400, 0, 0),
				new Agent(Faction.ENL, -400, -50, -Math.PI/6),
				new Agent(Faction.RES, 0, 700, 0)
				};
	}

	@Override
	public Portal[] getPortals() {
		Random rr = new Random(135769);
		List<Portal> rtn = new LinkedList<>();
		Portal first = new Portal(0,0, Color.gray);
		rtn.add(first);
		Portal[] ends = new Portal[3];
		for (int wing=0; wing<3; wing++) {
			for (int p=0; p<getNumberOfPortalsPerBranch(); p++)  {
				double theta = wing * Math.PI*2/3;
				double r = 400.0 / getNumberOfPortalsPerBranch()*(p+1);
				double x = r*Math.cos(theta)+jitter(rr);
				double y = r*Math.sin(theta)+jitter(rr);
				Portal portal = new Portal(x, y, Color.gray);
				rtn.add(portal);
				ends[wing] = portal;
			}
		}
		rr = new Random(1833);
		while (rtn.size() < 20) {
			double[] c = ends[0].getPt();
			double[] b = ends[1].getPt();
			double[] a = ends[2].getPt();
			double xx = rr.nextGaussian();
			double[] p = {xx*300, rr.nextGaussian()*300};
			if (GeomUtils.pointInTriangle(p, a, b, c)) {
				rtn.add(new Portal(p[0], p[1], Color.gray));
			}
		}
		return rtn.toArray(new Portal[0]);
	}

	
	private double jitter(Random rr) {
		return rr.nextGaussian()*5;
	}

	@Override
	protected double getAreaPerMU() {
		Portal[] histories = getPortals();
		Portal[] branches = new Portal[3];
		for (int wig=0 ; wig<3; wig++) {
			int i=getNumberOfPortalsPerBranch()-1; 
			branches[wig] = histories[1+wig*getNumberOfPortalsPerBranch()+i];
		}

		double area = GeomUtils.getArea(
				branches[0].getPt(), 
				branches[1].getPt(), 
				branches[2].getPt()); 
		return area/1000;
	}

}
