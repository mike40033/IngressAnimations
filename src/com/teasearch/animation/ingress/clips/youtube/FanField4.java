package com.teasearch.animation.ingress.clips.youtube;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.Instruction;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.clips.FanFieldData;
import com.teasearch.animation.ingress.clips.FanType;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.utils.GeomUtils;

public class FanField4 extends IngressAnimableConstantMU{

	@Override
	protected Agent[] getAgents() {
		return new Agent[] {
				new Agent(Faction.MAUVE.getColor(), -600, -100, 0),
		};
	}

	@Override
	protected Portal[] getPortals() {
		Random rr = new Random(3232662224L);
		int n = getNumberOfPortalsExcludingAnchor();
		LinkedList<Portal> pp = new LinkedList<>();
		double p1X = 200;
		double p1Y = -400;
		double p2X = -500;
		double p2Y = 200;
		double p3X = 600;
		double p3Y = 300;
		pp.add(new Portal(p1X,p1Y, Faction.MAUVE.getColor()));
		pp.add(new Portal(p2X,p2Y, Color.gray));
		pp.add(new Portal(p3X,p3Y, Color.gray));
		for (int i=3; i<=n; i++) {
			double theta = Math.PI/2*(i-2)/(n-1)+rr.nextGaussian()*0.02*Math.PI/2/n;
			double r = rr.nextDouble()*0.7+0.2;
			double a= r*Math.cos(theta);
			double b= r*Math.sin(theta);
			double x = p1X + (p2X-p1X)*a + (p3X-p1X)*b;
			double y = p1Y + (p2Y-p1Y)*a + (p3Y-p1Y)*b;
			pp.add(new Portal(x, y, i == 6 ? Color.black: Color.gray));
		}
		return pp.toArray(new Portal[0]);
	}

	private int getNumberOfPortalsExcludingAnchor() {
		return 8;
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
				true,
				FanType.concurrent,
				true,
				false,
				0.5,
				0.5,
				2.0
				);
		List<Instruction> instructions = fanFieldData.getInstructions();
		l1.addInstructions(instructions.toArray(new Instruction[0]));
		l1.addInstruction(new GoTo(-800,300, false));
	}

}
