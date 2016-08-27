package com.teasearch.animation.ingress.clips.youtube;

import java.awt.Color;
import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.Instruction;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.FanFieldData;
import com.teasearch.animation.ingress.clips.FanType;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.utils.GeomUtils;

public class FanField1 extends IngressAnimableConstantMU{
	@Override
	protected Agent[] getAgents() {
		return new Agent[] {
				new Agent(Faction.LEMON.getColor(), -600, -100, 0),
				new Agent(Faction.RED.getColor(), -850, 200, 0),
		};
	}

	@Override
	protected Portal[] getPortals() {
		Random rr = new Random(332234);
		int n = getNumberOfPortalsExcludingAnchor();
		LinkedList<Portal> pp = new LinkedList<>();
		pp.add(new Portal(0,400, Color.gray));
		pp.add(new Portal(-600,-300, Faction.LEMON.getColor()));
		pp.add(new Portal(600,-300, Faction.LEMON.getColor()));
		for (int i=3; i<=n; i++) {
			double theta = Math.PI/2*(i-2)/(n-1)+rr.nextGaussian()*0.02*Math.PI/2/n;
			double r = rr.nextDouble()*0.8+0.1;
			double a= r*Math.cos(theta);
			double b= r*Math.sin(theta);
			double x = -600*a+700*b;
			double y = -600*a - 700*b + 400;
			while (!GeomUtils.pointInTriangle(new double[]{x,y}, new double[]{0,400}, new double[]{-600,-300}, new double[]{600,-300})) {
				r = rr.nextDouble();
				a= r*Math.cos(theta);
				b= r*Math.sin(theta);
				 x = -600*a+700*b;
				y = -(600*a + 700*b - 400);
			}
			pp.add(new Portal(x, y, Faction.LEMON.getColor()));
		}
		return pp.toArray(new Portal[0]);
	}

	private int getNumberOfPortalsExcludingAnchor() {
		return 9;
	}

	@Override
	protected double getAreaPerMU() {
		Portal[] pp = getPortals();
		return GeomUtils.getArea(pp[0].getPt(), pp[1].getPt(), pp[2].getPt())/1000;
	}

	@Override
	protected void addInstructions(InstructionList[] lists,
			PortalHistory[] histories) {
		InstructionList l1 = lists[0];
		
		List<PortalHistory> others = new LinkedList<>();
		for (int i=1; i<histories.length; i++) {
			others.add(histories[i]);
		}
		FanFieldData fanFieldData = FanFieldData.createFanfieldData(
				histories[0], 
				true, 
				others,
				!true,
				FanType.out,
				true,
				true,
				0.5,
				0.5,
				2.0
				);
		List<Instruction> instructions = fanFieldData.getInstructions();
		l1.addInstructions(instructions.toArray(new Instruction[0]));
		
		InstructionList l2 = lists[1];
		l2.addInstruction(Wait._for(73, true));
		l2.addInstruction(new CapturePortal(histories[0]));
		l1.addInstruction(Wait._for(4, true));
		l2.addInstruction(new GoTo(-800,500, false));
		l1.addInstruction(new CapturePortal(histories[0]));
		l1.addInstruction(new LinkPortals(histories[0], 2, fanFieldData.getRebuildLinkOrder()));
		l1.addInstruction(new GoTo(-800,300, false));
	}

}
